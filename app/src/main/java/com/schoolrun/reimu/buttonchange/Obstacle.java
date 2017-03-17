package com.schoolrun.reimu.buttonchange;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Reimu on 2016-02-28.
 */
public class Obstacle extends ImageView {
    final int INITIAL_TIME=2500;
    RelativeLayout r;
    float screenWidth;
    ObjectAnimator ta;
    Obstacle(Context context){

        super(context);
        setImage(context.getSharedPreferences("high",0).getString("selectedPlayer","pink"));
    }
    void setImage(String s){
        if(s.equals("yellow")||s.equals("pink")){
            Drawable d= ResourcesCompat.getDrawable(getResources(), R.drawable.obs, null);
            setImageDrawable(d);
        }
        else{
            Drawable d= ResourcesCompat.getDrawable(getResources(), R.drawable.violinobs, null);
            setImageDrawable(d);
        }

    }

    void move(int score) {
        ta=ObjectAnimator.ofFloat(this,"translationX",screenWidth,-30f);
        ta.setInterpolator(new LinearInterpolator());
        ta.setDuration(Math.max(1000,INITIAL_TIME-score*15));
        ta.start();
        ta.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                r.removeView(Obstacle.this);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

}
