package com.schoolrun.reimu.buttonchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * Created by Reimu on 2016-06-23.
 */
public class Shop extends AppCompatActivity{
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    int w;
    int h;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        pref=this.getSharedPreferences("high",0);
        final SharedPreferences.Editor editor = pref.edit();
        setContentView(R.layout.shopmenu);
        final int height = pref.getInt("buttonHeight",0);
        final int width = pref.getInt("buttonWidth",0)*2/3;
        final Typeface CUSTOM_FONT = Typeface.createFromAsset(getAssets(), "fonts/Molot.otf");
        final RelativeLayout RE = (RelativeLayout) findViewById(R.id.shopr);
        ScrollView l = (ScrollView) findViewById(R.id.shoplist);

        h = pref.getInt("screenHeight",0);
        w = pref.getInt("screenWidth",0);
        final LinearLayout MENU = (LinearLayout) findViewById(R.id.shopmenu);
        LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(w/2, ViewGroup.LayoutParams.WRAP_CONTENT);
        menuParams.leftMargin= w/2;

        RelativeLayout.LayoutParams mascotParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mascotParams.setMargins(w/10,h/7,w*3/5,h/8);
        ImageView mascot = new ImageView(this);
        mascot.setImageResource(R.drawable.mascot);
        RE.addView(mascot,mascotParams);
        final Button inventory = new Button(this);
        Button one = new Button(this);
        Button two = new Button(this);
        Button five = new Button(this);
        Button ten = new Button(this);
        Button back = new GameButton(this,"back",height,width);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Shop.this,StartMenu.class));
            }
        });
        RE.addView(back);
        final Button[] list = {inventory,one,two,five,ten};
        final int[][] prices ={{1,200},{2,300},{5,600},{10,1000}};
        RE.post(new Runnable() {
                    public void run() {
                        int index = 0;

                        for (Button b : list) {
                            b.setTypeface(CUSTOM_FONT);


                            if (index != 0) {
                                final int i = index-1;
                                b.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {

                                                             final RelativeLayout pop = new RelativeLayout(Shop.this);
                                                             final int popw = w / 2;
                                                             final int poph = w/4;
                                                             RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                             textParams.setMargins(w/20,w/35,0,0);
                                                             final TextView t = new TextView(Shop.this);
                                                             t.setText("Buy " + Integer.toString(prices[i][0]) + " lessons for "
                                                                     + Integer.toString(prices[i][1]) + " coins?");
                                                             t.setTextColor(Color.WHITE);
                                                             t.setShadowLayer(5,-1,1,Color.parseColor("#17ead9"));
                                                             t.setTextSize(COMPLEX_UNIT_PX,h/17);


                                                             t.setTypeface(CUSTOM_FONT);
                                                             RelativeLayout.LayoutParams yesLay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                             RelativeLayout.LayoutParams noLay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                             yesLay.setMargins(popw / 10, poph / 5 * 3, 0, 0);
                                                             noLay.setMargins(popw /5*3, poph / 5 * 3, 0, 0);
                                                             final Button yes = new GameButton(Shop.this,"other","YES",height,width);
                                                             yes.setLayoutParams(yesLay);
                                                             final Button no = new GameButton(Shop.this,"other","NO",height,width);
                                                             final PopupWindow p = new PopupWindow(pop, popw, poph, true);

                                                             yes.setOnClickListener(new View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     if (pref.getInt("money", 0) >= prices[i][1]) {
                                                                         editor.putInt("money", pref.getInt("money", 0) - prices[i][1]);
                                                                         editor.putInt("tutorAmount", pref.getInt("tutorAmount", 0) + prices[i][0]);
                                                                         editor.commit();
                                                                         inventory.setText("Current lessons: " + Integer.toString(pref.getInt("tutorAmount", 0))
                                                                                 + "\n Coins obtained: " + Integer.toString(pref.getInt("money", 0)));
                                                                         p.dismiss();
                                                                     } else {
                                                                         t.setText("You do not have enough coins");
                                                                         pop.removeView(yes);
                                                                         pop.removeView(no);
                                                                         final Button ok = new GameButton(Shop.this,"other","OK",height,width);
                                                                         RelativeLayout.LayoutParams okLay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                                         okLay.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                                                         okLay.topMargin = poph*3/5;
                                                                         pop.addView(ok, okLay);
                                                                         ok.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 p.dismiss();
                                                                             }
                                                                         });

                                                                     }
                                                                 }
                                                             });
                                                             no.setOnClickListener(new View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     p.dismiss();
                                                                 }
                                                             });
                                                             pop.setBackgroundResource(R.drawable.popupbg);
                                                             pop.addView(yes, yesLay);
                                                             pop.addView(no, noLay);
                                                             pop.addView(t, textParams);
                                                             p.setContentView(pop);
                                                             p.showAtLocation(RE, 0, w / 4, h / 4);

                                                         }
                                                     }
                                );
                            }


                            if (index == 0) {
                                b.setText("Current lessons: " + Integer.toString(pref.getInt("tutorAmount", 0))
                                        + "\n Coins obtained: " + Integer.toString(pref.getInt("money", 0)));
                                b.setBackgroundResource(R.drawable.buttoncornered);
                                b.setTextColor(Color.WHITE);

                            } else {
                                int i = index - 1;
                                b.setTextColor(Color.parseColor("#17ead9"));
                                Drawable img = ResourcesCompat.getDrawable(getResources(), R.drawable.shopicon, null);
                                img.setBounds( 0, 0,w/15, h/15 );
                                b.setCompoundDrawables(img, null,null,null);
                                b.setBackgroundResource(R.drawable.buttonshape);
                                b.setText(Integer.toString(prices[i][0]) + " lessons\n\t" + Integer.toString(prices[i][1]) + "coins needed");
                            }
                            ++index;
                            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(w*11/25,ViewGroup.LayoutParams.MATCH_PARENT);
                            l.setMargins(0,0,0,h/30);
                            b.setLayoutParams(l);
                           MENU.addView(b);
                        }
                    }
                }
        );
    }
    }


