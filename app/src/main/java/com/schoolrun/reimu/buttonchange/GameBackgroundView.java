package com.schoolrun.reimu.buttonchange;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

/**
 * Created by Reimu on 2016-12-10.
 */

public class GameBackgroundView extends View{
    int h;
    int w;
    Typeface custom_font;
    Canvas canvas;
    Paint inside;
    Paint outline;
    int x;
    int y;
    String text;

    GameBackgroundView(Context context,String text,int x, int y,int textSize) {
        super(context);
        this.text = text;
        this.x = x;
        this.y = y;
        SharedPreferences pref = context.getSharedPreferences("high",0);
        h = pref.getInt("screenHeight",0);
        w = pref.getInt("screenWidth",0);
        custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Molot.otf");
        outline = new Paint();
        outline.setColor(ResourcesCompat.getColor(getResources(), R.color.textColor, null));
        outline.setTextSize(textSize);
        outline.setTypeface(custom_font);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(15);
        inside= new Paint();
        inside.setColor(Color.WHITE);
        inside.setTextSize(textSize);
        inside.setTypeface(custom_font);
        setWillNotDraw(false);

    }


    void updateText(String s){
        this.text = s;

        invalidate();

    }
    @Override
    public void onDraw(Canvas canvas){

        canvas.drawText(text,x,y,outline);
        canvas.drawText(text,x,y,inside);
        //draw(canvas);

    }



}
