package com.schoolrun.reimu.buttonchange;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Reimu on 2016-05-16.
 */
public class Cloud extends ImageView {
    final int INITIAL_TIME=30000;
    RelativeLayout r;
    float screenWidth;
    ObjectAnimator ta;
    Cloud(Context context){
        super(context);
        setBackgroundResource(R.drawable.cloudobj);
    }

    void move() {
        ta=ObjectAnimator.ofFloat(this,"translationX",screenWidth,-1000);
        ta.setInterpolator(new LinearInterpolator());
        ta.setRepeatCount(ValueAnimator.INFINITE);
        ta.setDuration(INITIAL_TIME);
        ta.start();

        ta.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {



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

