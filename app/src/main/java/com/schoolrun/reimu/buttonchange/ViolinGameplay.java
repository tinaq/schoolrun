package com.schoolrun.reimu.buttonchange;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by kqian on 10/13/16.
 */

public class ViolinGameplay extends MainActivity {
    Queue<Buttonn> buttonll = new LinkedList<Buttonn>();
    RelativeLayout.LayoutParams buttonLayout;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        start.setBackgroundResource(R.drawable.vioilnbg);
        buttonLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    //actually this method returns whether or not they should keep on checkign intersection... return false means keep checking
    // true means stop checking
    public boolean isGameOver(Obstacle ob) {
        boolean bulletCollided = false;
        Buttonn b = null;
        if (!buttonll.isEmpty()) {
            b = buttonll.peek();
            if (b.getX() + b.getWidth() >= w) {
                buttonll.poll();
                start.removeView(b);
            } else {
                bulletCollided = bulletIntersect(ob, b);
            }
        }

        boolean playerCollided = intersect(ob);
        // bullet hit obs when red   game over
        if (!button.canJump) {
            if (bulletCollided) {
                start.removeView(ob);
                start.removeView(b);
                buttonll.poll();
                gameOver = true;
                return true;
            }
            // obs hit player when red
            if (playerCollided) {
                ++score;
                scoreView.updateText(String.valueOf(score));
                Random r = new Random();
                int x = r.nextInt(2);
                if (x == 1) {
                    button.change(true);
                } else {
                    button.change(false);
                }
                start.removeView(ob);
                return true;
            }
        } else {
            //bullet hit obs when green
            if (bulletCollided) {
                ++score;
                scoreView.updateText(String.valueOf(score));
                Random r = new Random();
                int x = r.nextInt(2);
                if (x == 1) {
                    button.change(true);
                } else {
                    button.change(false);
                }
                start.removeView(b);
                start.removeView(ob);
                buttonll.poll();
                return true;
            }
            //obs hit player when green
            else if (playerCollided) {
                start.removeView(ob);
                gameOver = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean intersect(Obstacle ob) {
        int[] plCoords = {0, 0};
        player.getLocationOnScreen(plCoords);
        int[] obCoords = {0, 0};
        ob.getLocationOnScreen(obCoords);
        if (plCoords[0] >= obCoords[0]) {
            return true;
        }
        return false;

    }

    public boolean bulletIntersect(Obstacle ob, Buttonn b) {
        int[] plCoords = {0, 0};
        b.getLocationOnScreen(plCoords);
        int[] obCoords = {0, 0};
        ob.getLocationOnScreen(obCoords);
        if (plCoords[0] >= obCoords[0]) {
            return true;
        }
        return false;
    }
   @Override
   public void addSpecialViews(){
       
   }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (!userStart) {
                addObstacle(start).start();
                userStart = true;
                start.removeView(instructionsView);
            }
            if (!gameOver && userStart) {
                Buttonn b = ((Violin) player).shoot();
                buttonll.add(b);
                start.addView(b, buttonLayout);
                player.setScaleType(ImageView.ScaleType.FIT_START);
            }
        }

        return gl.onTouchEvent(e);
    }

    @Override
    public int setSpeed() {
        Random r = new Random();
        int y = Math.min(score, 50);
        int x = r.nextInt(50) + 750 + y;
        return x;
    }

    @Override
    public Thread addObstacle(final RelativeLayout start) {
        obLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayout.setMargins(w / 8 + player.getWidth() * 9 / 10, player.y + player.getHeight() / 3, 0, 0);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Obstacle ob = new Obstacle(ViolinGameplay.this);
                obsArray.add(ob);
                ob.r = start;
                ob.setAdjustViewBounds(true);
                ob.setScaleType(ImageView.ScaleType.FIT_END);
                ob.screenWidth = w;
                start.addView(ob,obLayout);
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
                obLayout.setMargins(0, player.y + player.getHeight()*2/5, 0, h/12);
                Log.i("k",Integer.toString(player.y));
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
}
