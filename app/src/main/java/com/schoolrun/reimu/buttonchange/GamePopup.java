package com.schoolrun.reimu.buttonchange;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * Created by Reimu on 2016-11-04.
 */

public class GamePopup extends PopupWindow {
    private String buttonText;
    private String popupText;
    private Context context;
    public Button button;
    int w;
    int h;
    int bh;

    GamePopup(String buttonText, String popupText,Context context){
        this.buttonText=buttonText;
        this.popupText=popupText;
        this.context = context;
        SharedPreferences pref = context.getSharedPreferences("high", 0);
         w = pref.getInt("screenWidth",0);
        h = pref.getInt("screenHeight",0);
        bh = pref.getInt("buttonHeight",0);
        this.setWidth(w/2);
        this.setHeight(w/4);
        this.setContentView(setPopupLayout());
    }
    private RelativeLayout setPopupLayout(){
        RelativeLayout r = new RelativeLayout(context);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(w/20,w/35,0,0);
        buttonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        buttonParams.topMargin=w/4-bh*11/10;
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Molot.otf");
        TextView t = new TextView(context);

        t.setTypeface(custom_font);
        t.setTextSize(COMPLEX_UNIT_PX,h/17);
        t.setLayoutParams(textParams);
        t.setText(popupText);
        t.setTextColor(Color.WHITE);
        t.setShadowLayer(5,-1,1,Color.parseColor("#17ead9"));
        r.setBackgroundResource(R.drawable.popupbg);

        button = new GameButton(context,"other",buttonText);
        button.setLayoutParams(buttonParams);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GamePopup.this.dismiss();
            }
        });
        r.addView(button);
        r.addView(t);
       return r;
    }


}
