package com.schoolrun.reimu.buttonchange;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;

/**
 * Created by Reimu on 2016-09-21.
 */

public class GameButton extends Button {
    int height;
    int width;
    SharedPreferences pref;
    Typeface custom_font;
    String type;
    Context context;
    int color;
    String text;
    public GameButton(Context context,String type) {
        super(context);
        this.context=context;
        this.type = type;
        pref=context.getSharedPreferences("high",0);
        this.height=pref.getInt("buttonHeight",0);
        width=pref.getInt("buttonWidth",0);
        color =R.color.textColor;
        init();
    }
    public GameButton(Context context,String type, String text){
        super(context);
        this.text = text;
        this.type=type;
        this.context=context;
        pref=context.getSharedPreferences("high",0);
        this.height=pref.getInt("buttonHeight",0);
        width=pref.getInt("buttonWidth",0);
        color =R.color.textColor;
        init();
    }
    public GameButton(Context context,String type, String text,int h, int w){
        super(context);
        this.text = text;
        this.type=type;
        this.context=context;
        pref=context.getSharedPreferences("high",0);
        this.height=h;
        width=w;
        color =R.color.textColor;
        init();
    }
    public GameButton(Context context,String type,int h, int w){
        super(context);
        this.type=type;
        this.context=context;
        pref=context.getSharedPreferences("high",0);
        height=h;
        width=w;
        color =R.color.textColor;
        setMinimumHeight(0);
        setMinimumWidth(0);
        init();
    }

    void init() {

        custom_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Molot.otf");

            setBackgroundResource(R.drawable.buttonshape);

        setHeight(height);
        setWidth(width);

        if(type.equals("start")){
            setOnClickListener(new OnStartActivityClickListener(context){
            });
        }
        else if (!type.equals("back")) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equals("select")) {
                        GameButton.this.getContext().startActivity(new Intent(GameButton.this.getContext(), Select.class));
                    } else if (type.equals("shop")) {
                        GameButton.this.getContext().startActivity(new Intent(GameButton.this.getContext(), Shop.class));
                    }
                    ((Activity) GameButton.this.getContext()).finish();
                }
            });
        }

    }
    public void onDraw(Canvas c){
        Paint paint = new Paint();
        paint.setColor(ResourcesCompat.getColor(context.getResources(), R.color.textColor, null));
        paint.setTextSize(height/2);
        paint.setTypeface(custom_font);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(10);
        Paint paintInside = new Paint();
        paintInside.setColor(Color.WHITE);
        paintInside.setTextSize(height/2);
        paintInside.setTypeface(custom_font);
        paintInside.setTextAlign(Paint.Align.CENTER);
        if(text==null) {
            if (type.equals("start")) {
                c.drawText("START", width / 2, (float) (height * 0.75 - 7.5), paint);
                c.drawText("START", width / 2, (float) (height * 0.75 - 7.5), paintInside);

            } else if (type.equals("select")) {
                c.drawText("SELECT", width / 2, (float) (height * 0.75 - 7.5), paint);
                c.drawText("SELECT", width / 2, (float) (height * 0.75 - 7.5), paintInside);
            } else if (type.equals("shop")) {
                c.drawText("SHOP", width / 2, (float) (height * 0.75 - 7.5), paint);
                c.drawText("SHOP", width / 2, (float) (height * 0.75 - 7.5), paintInside);
            } else if (type.equals("back")) {
                c.drawText("BACK", width / 2, (float) (height * 0.75 - 7.5), paint);
                c.drawText("BACK", width / 2, (float) (height * 0.75 - 7.5), paintInside);
            }
        }
        else{
            c.drawText( text,width/2,(float)(height*0.75-7.5), paint);
            c.drawText( text,width/2,(float)(height*0.75-7.5), paintInside);
        }
    }
}