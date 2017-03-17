package com.schoolrun.reimu.buttonchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class MainActivity extends AppCompatActivity {
    Player player;
    long time = 0;
    int textColor;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    boolean userStart = false;
    boolean gameOver = false;
    int w;
    int h;
    GestureDetector gl;
    Buttonn button;
    int score = 0;
    Typeface custom_font;
    RelativeLayout start;
    String s;
    RelativeLayout.LayoutParams obLayout;
    Queue<Obstacle> obsArray;
    GameBackgroundView scoreView;
    GameBackgroundView instructionsView;
    Random r = new Random ();
    RelativeLayout.LayoutParams obLargeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = this.getSharedPreferences("high", 0);
        textColor = ResourcesCompat.getColor(getResources(), R.color.textColor, null);
        s = pref.getString("selectedPlayer", "pink");
        w = pref.getInt("screenWidth", 0);
        h = pref.getInt("screenHeight", 0);
        custom_font = Typeface.

        player = GlobalMethodsSingleton.getPlayerByName(s, this);
        player.y = GlobalMethodsSingleton.getPlayerY(s, this);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/Molot.otf");
        setContentView(R.layout.ground);
        start = (RelativeLayout) findViewById(R.id.groundstate);
        instructionsView = new GameBackgroundView(this,"TAP TO START",w/3,h/2,h/10);
        scoreView = new GameBackgroundView(this,"0",w/50,h/10,h/10);
        obLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        obLargeLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        start.addView(instructionsView);
        start.addView(scoreView);

        editor = pref.edit();

        start.post(new Runnable() {
                       @Override
                       public void run() {
                           int i = r.nextInt(3);
                           if(i!=1) {
                               AdManager.load(MainActivity.this);
                           }
                           addViews(start);
                           gl = new GestureDetector(MainActivity.this, new MyGl());
                           start.setOnTouchListener(new View.OnTouchListener() {
                               @Override
                               public boolean onTouch(View v, MotionEvent event) {
                                   gl.onTouchEvent(event);
                                   return false;
                               }
                           });
                       }
                   }
        );


    }

    public void addViews(final RelativeLayout start) {
        addSpecialViews();
        button = new Buttonn(MainActivity.this);
        player.button = button;

        if (player.getParent() != null) {
            ((ViewGroup) (player.getParent())).removeView(player);
        } else {
            RelativeLayout.LayoutParams playerLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            playerLayout.setMargins(w / 10, player.y, 0, 0);
            player.x = w / 8;
            player.setLayoutParams(playerLayout);
            player.setScaleType(ImageView.ScaleType.FIT_START);
            player.setAdjustViewBounds(true);
        }

        start.addView(player);
        RelativeLayout.LayoutParams buttonLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayout.setMargins(w / 8, player.y, 0, 0);
        button.setLayoutParams(buttonLayout);
        button.setScaleType(ImageView.ScaleType.FIT_START);
        //layout of the player

        start.addView(button);
        ((AnimationDrawable) player.getDrawable()).start();
        obsArray = new LinkedList<>();
    }

    public void addSpecialViews() {
        RelativeLayout.LayoutParams cloudLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cloudLayout.setMargins(0, h / 8, 0, 0);
        Cloud cloud = new Cloud(MainActivity.this);
        cloud.screenWidth = w;
        start.addView(cloud, cloudLayout);
        cloud.move();

    }

    public Thread addObstacle(final RelativeLayout start) {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                int x = 1;
                if (s.equals("yellow")) {
                    x = (int) (Math.random() * 3);
                }
                final Obstacle ob;
                obLayout.setMargins(0, player.y+player.getHeight()*3/5, 0, 0);
                obLargeLayout.setMargins(0, player.y + player.getHeight() /2, 0, 0);
                if (x != 0) {
                    ob = new Obstacle(MainActivity.this);
                    ob.setLayoutParams(obLayout);
                } else {
                    ob = new TallerObstacle(MainActivity.this);
                    ob.setLayoutParams(obLargeLayout);
                }
                obsArray.add(ob);

                ob.r = start;

                ob.setAdjustViewBounds(true);
                ob.setScaleType(ImageView.ScaleType.FIT_END);
                ob.screenWidth = w;
                start.addView(ob);
                ob.move(score);
                final Thread th = new Thread() {
                    boolean done = false; // used to prevent infinite adds

                    @Override
                    public void run() {
                        while (!done && !gameOver) {
                            final Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    done = isGameOver(ob);

                                }
                            };
                            start.post(r);
                            start.removeCallbacks(r);
                        }
                    }
                };
                th.start();
            }
        };
        Thread t = new Thread() {
            @Override
            public void run() {

                while (!gameOver) {

                    try {
                        int x = setSpeed();
                        Thread.sleep(x);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(runnable);
                    start.removeCallbacks(runnable);
                }
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      if (!(player.getDrawable() instanceof BitmapDrawable)) {
                                          ((AnimationDrawable) player.getDrawable()).stop();
                                      }
                                      clearObsArray();
                                      popup(start);
                                  }
                              }
                );
            }
        };
        return t;
    }

    public int setSpeed() {

        int y = Math.min(score, 50);
        int x = r.nextInt(800) + 800 + y;
        return x;
    }

    public boolean isGameOver(Obstacle ob) {
        float y = (float) ob.ta.getAnimatedValue();
        boolean collided = intersect(ob);
        if (collided && !button.canJump) {
            ++score;
            scoreView.updateText(String.valueOf(score));

            int x = r.nextInt(2);
            if (x == 1) {
                button.change(false);
            }
            obsArray.poll();
            return true;
        } else if (collided) {
            gameOver = true;
            Log.i("over","1");

            return true;
        } else if (y <= player.getX() && button.canJump) {
            ++score;
            scoreView.updateText(String.valueOf(score));
            int x = r.nextInt(2);
            if (x == 1) {
                button.change(true);
            }
            obsArray.poll();
            return true;
        } else if (y <= player.getX() && !button.canJump) {
            //game over
            gameOver = true;
            Log.i("over","2");
            return true;
        }
        return false;
    }


    public void clearObsArray() {
        while (!obsArray.isEmpty()) {
            start.removeView(obsArray.poll());
        }
    }


    public void gameOverScreen() {
        int hs = (pref.getInt("high", 0));
        if (score > hs) {
            editor.putInt("high", score);
        }
        int m = pref.getInt("money", 0);
        editor.putInt("money", score + m);
        editor.putInt("current", score);
        if (score > 5 && !pref.getBoolean("yellowunlocked", false)) {
            editor.putBoolean("yellowunlocked", true);
            editor.putBoolean("newCharacter", true);
        }
        editor.commit();
        startActivity(new Intent(MainActivity.this, EndScreen.class));
        finish();
        System.exit(0);
    }

    public boolean intersect(Obstacle o) {
        int[] plCoords = {0, 0};
        player.getLocationOnScreen(plCoords);
        int[] obCoords = {0, 0};
        o.getLocationOnScreen(obCoords);
        double plBottom = plCoords[1] + player.getHeight();
        if (obCoords[0] >= plCoords[0] + player.getWidth() * 0.6 && obCoords[0] <= plCoords[0] + player.getWidth() * 0.75) {
            if (plBottom >= obCoords[1]) {
                Log.i("over","3");
                return true;
            }
        } else if (obCoords[0] >= plCoords[0] + player.getWidth() * 0.38 && obCoords[0] <= plCoords[0] + player.getWidth() * 0.6) {
            double plBottom2 = plCoords[1] + player.getHeight() * 0.67;
            if (plBottom2 >= obCoords[1]) {
                Log.i("over","4");
                return true;
            }
        }
        return false;

    }

    public void popup(final RelativeLayout v) {
       final RelativeLayout r = new RelativeLayout(this);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams buttonParamsNo = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(0, w / 40, 0, 0);
        buttonParams.setMargins(w /25*8, w / 8, 0, 0);
        buttonParamsNo.setMargins(w / 24, w / 8, 0, 0);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        final TextView t = new TextView(this);
        t.setText("Ask tutor for help?\nYou have "+pref.getInt("tutorAmount",0)+" left");
        t.setTextColor(Color.WHITE);
        t.setShadowLayer(5,-1,1,textColor);
        t.setTypeface(custom_font);
        t.setTextSize(COMPLEX_UNIT_PX,h/17);
        t.setLayoutParams(textParams);
        final int height = pref.getInt("buttonHeight",0);
        final int width = pref.getInt("buttonWidth",0)*2/3;
        final Button ok = new GameButton(this,"other","YES",height,width);
        ok.setLayoutParams(buttonParams);
        final Button no = new GameButton(this,"other","NO",height,width);
        no.setLayoutParams(buttonParamsNo);
        final PopupWindow p = new PopupWindow(r, w / 2, w/4, true);
        p.setFocusable(false);
        p.setOutsideTouchable(false);
        ok.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                      int tut = pref.getInt("tutorAmount", 0);

                                      if (tut > 0) {
                                          p.dismiss();
                                          --tut;
                                          gameOver = false;
                                          addObstacle(v).start();
                                          editor.putInt("tutorAmount", tut - 1);
                                          editor.apply();
                                          ((AnimationDrawable) player.getDrawable()).start();
                                      } else {
                                          t.setText("You do not have enough");
                                          r.removeView(no);
                                          r.removeView(ok);
                                          GameButton ok = new GameButton(MainActivity.this,"other","ok",height,width);
                                          buttonParams.leftMargin= w/4-width/2;
                                          ok.setLayoutParams(buttonParams);
                                          r.addView(ok);
                                          ok.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  p.dismiss();
                                                  InterstitialAd ad = AdManager.getAd();
                                                  if(ad!=null&&ad.isLoaded()) {
                                                          ad.show();
                                                          editor.putBoolean("playedAd",true);
                                                          editor.apply();

                                                          ad.setAdListener(new AdListener() {
                                                              @Override
                                                              public void onAdClosed() {

                                                                  gameOverScreen();
                                                              }
                                                          });
                                                  }
                                                  else {
                                                      gameOverScreen();
                                                  }
                                              }
                                          });

                                      }
                                  }
                              }

        );
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.dismiss();
                InterstitialAd ad = AdManager.getAd();
                if(ad!=null&&ad.isLoaded()) {
                    Log.i("ad","not null");
                        ad.show();
                    editor.putBoolean("playedAd",true);
                    editor.apply();
                        ad.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                gameOverScreen();
                            }
                        });
                    }
                else {
                    gameOverScreen();
                }
            }
        });
        r.setBackgroundResource(R.drawable.popupbg);
        r.addView(ok);
        r.addView(t);
        r.addView(no);
        p.setContentView(r);
        p.showAtLocation(v, 0, w / 4, h / 4);
    }

    class MyGl extends GestureDetector.SimpleOnGestureListener {
        boolean longPress = false;

        @Override
        public boolean onDown(MotionEvent event) {
            longPress = false;
            return true;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    boolean longJump = false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.w("debug","oncreate12");
        final Runnable doubleJump = new Runnable() {
            public void run() {
                if (player.getY() == player.y && !gameOver && userStart && longJump) {
                    player.doubleJump();
                    button.animation = h / 2;
                    player.setScaleType(ImageView.ScaleType.FIT_START);
                    button.move();
                    longJump = false;
                }
            }
        };
        final Thread t = new Thread() {
            @Override
            public void run() {
                while (!gameOver && longJump && s.equals("yellow")) {
                    try {
                        Thread.sleep(130);
                        runOnUiThread(doubleJump);
                    } catch (Exception e) {
                    }
                }
                start.removeCallbacks(doubleJump);
            }
        };
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                longJump = true;
                t.start();
                time = System.currentTimeMillis();
                if (!userStart) {
                    start.removeView(instructionsView);
                    userStart = true;
                    addObstacle(start).start();

                }
                if (s.equals("pink") && player.getY() == player.y && !gameOver && userStart) {
                    player.jump();
                    player.setScaleType(ImageView.ScaleType.FIT_START);
                    button.move();
                }
                break;


            case MotionEvent.ACTION_UP:
                if (s.equals("yellow") && System.currentTimeMillis() - time < 130) {
                    if (player.getY() == player.y && !gameOver && userStart) {
                        player.jump();
                        player.setScaleType(ImageView.ScaleType.FIT_START);
                        button.move();
                        longJump = false;
                    }
                }
                break;
        }

        return gl.onTouchEvent(e);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy(){

    }
}
