package com.schoolrun.reimu.buttonchange;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_PX;


/**
 * Created by Reimu on 2016-08-16.
 */
public class VisualNovel extends FrameLayout{
    String[][] story;
    int H;
    int W;
    int L;
    int index =0;
    TextView text;
    ImageView character;
    View v;
    Context context;


VisualNovel(Context context,String[][]story,int length){
    super(context);
    this.context=context;
    this.story=story;
    SharedPreferences p = context.getSharedPreferences("high",0);
    H=p.getInt("screenHeight",0);
    W=p.getInt("screenWidth",0);
    init(context);
    this.setBackgroundResource(R.drawable.bg2);
    this.L = length;
}


    public void init(final Context context) {

        LayoutInflater l = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         v = l.inflate(R.layout.visualnovel, this, true);
        final RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        text = (TextView) v.findViewById(R.id.story);
        text.setTextColor(Color.WHITE);
        text.setTextSize(COMPLEX_UNIT_PX,H/20);
        RelativeLayout textbox = (RelativeLayout)v.findViewById(R.id.textbox) ;
        textbox.getLayoutParams().height=H*28/100;
        character = (ImageView) v.findViewById(R.id.chara);
                textParams.setMargins(W / 12, H /32, W / 12, H /32);
                text.setLayoutParams(textParams);
                imageParams.setMargins(W / 7 * 2, H/ 10, 0, 0);
                character.setLayoutParams(imageParams);
                character.setImageResource(Integer.parseInt(story[index][1]));
        text.setText(story[0][0]);
        this.setOnClickListener(new OnStartActivityClickListener(context) {
            @Override
            public void onClick(View v) {
                if(hasMoreStory()){
                    updateStory();
                }
                else {
                    Fragment fragment = ((Activity) context).getFragmentManager().findFragmentByTag("vn");
                    if (StringResources.startGame) {
                        this.startGame(context.getSharedPreferences("high", 0).getString("selectedPlayer", "pink"));
                    }
                    else {

                        ((Activity) context).getFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                }
            }
        });
    }
public void updateStory(){
    if (index < L - 1) {
        text.setText(story[index + 1][0]);
        if (Integer.parseInt(story[index][1]) != Integer.parseInt(story[index + 1][1])) {
            character.setImageResource(Integer.parseInt(story[index + 1][1]));
        }
    }
        ++index;
}
    public Boolean hasMoreStory() {
                if (index < L - 1) {
                    return true;
                }
        return false;
            }
}

