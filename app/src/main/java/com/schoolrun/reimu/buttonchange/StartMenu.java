package com.schoolrun.reimu.buttonchange;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ImageView;

import com.google.android.gms.ads.MobileAds;

import static android.util.TypedValue.COMPLEX_UNIT_PX;


/**
 * Created by Reimu on 2016-05-15.
 */
public class StartMenu extends AppCompatActivity {
    Player p;
    StringResources sr;
    RelativeLayout r;
    SharedPreferences pref;
    Button b;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.startmenu);
       r = (RelativeLayout) findViewById(R.id.startmenu);


     pref = this.getSharedPreferences("high", 0);
       final SharedPreferences.Editor editor = pref.edit();

           r.post(new Runnable() {
                      @Override
                      public void run() {
                          if (pref.getInt("screenHeight", 0) == 0) {
                              final int width = r.getWidth();
                              final int h = width * 9 / 16;
                              int ButtonHeight = h * 2 / 15;
                              int ButtonWidth = ButtonHeight * 3;
                              editor.putInt("screenHeight", h);
                              editor.putInt("screenWidth", width);
                              editor.putInt("buttonHeight", ButtonHeight);
                              editor.putInt("buttonWidth", ButtonWidth);
                              editor.commit();

                          }
                          addViews(r);


                      }
                  }
           );
       }
    public void addViews(final RelativeLayout r){
        //start button
        final int h = pref.getInt("screenWidth",0)*9/16;
        r.getLayoutParams().height = h;
        final int width = pref.getInt("screenWidth",0);
        int ButtonWidth = pref.getInt("buttonWidth",0);
                final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
                animation.setDuration(800); // duration - half a second
                animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                animation.setRepeatMode(Animation.REVERSE);
        final ImageView gameLogo = new ImageView(this);
        gameLogo.setImageResource(R.drawable.gamelogo);
        RelativeLayout.LayoutParams logoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        logoParams.setMargins(width / 2 ,h/16, width / 20, 0);
        gameLogo.setAdjustViewBounds(true);
        gameLogo.setScaleType(ImageView.ScaleType.FIT_START);


        gameLogo.setLayoutParams(logoParams);
        r.addView(gameLogo);
                b = new GameButton(this, sr.START);

                RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                buttonParams.setMargins(width / 100 * 63, h / 100 * 57, 0, 0);
                r.addView(b, buttonParams);
                b.startAnimation(animation);
                Button select = new GameButton(this, sr.SELECT);
                Button shop = new GameButton(this,  sr.SHOP);
                RelativeLayout.LayoutParams selectParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams shopParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                selectParams.setMargins(width / 100 * 63 - ButtonWidth * 3 / 5, h / 100 * 77, 0, 0);
                shopParams.setMargins(width / 100 * 52 + ButtonWidth, h / 100 * 77, 0, 0);

                select.setLayoutParams(selectParams);

                r.addView(select);
                r.addView(shop, shopParams);

                Button help = new Button(this);
                RelativeLayout.LayoutParams helpParams = new  RelativeLayout.LayoutParams(width/18,width/18);
                helpParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                helpParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                help.setBackgroundResource(R.drawable.roundbutton);
                help.setText("?");
                help.setTextSize(COMPLEX_UNIT_PX,width/40);
                help.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Molot.otf"));
                help.setTextColor(Color.parseColor("#17ead9"));
                help.setMinHeight(0);
                help.setMinWidth(0);
                help.setGravity(Gravity.CENTER_VERTICAL);
                r.addView(help,helpParams);
                final GamePopup helpPop = new GamePopup("ok","See Select > long-click character > Intro \nInstructions are in the intro.",this);
                help.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helpPop.showAtLocation(r,0,width/4,h/4);
                    }
                });

                String s = pref.getString("selectedPlayer", "pink") + "cover";
                final ImageView girl = new ImageView(this);

                girl.setImageResource(getResources().getIdentifier(s, "drawable", getPackageName()));
                RelativeLayout.LayoutParams girlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                girlParams.setMargins(width / 65, h / 15, width / 5 * 3, 0);
                girl.setLayoutParams(girlParams);
                girl.setScaleType(ImageView.ScaleType.FIT_END);

                r.addView(girl);

                MobileAds.initialize(getApplicationContext(), "ca-app-pub-9876383615346337/5933692006");





    }
}
