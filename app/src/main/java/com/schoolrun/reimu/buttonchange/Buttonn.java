package com.schoolrun.reimu.buttonchange;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by Reimu on 2016-02-28.
 */
public class Buttonn extends ImageView{
    boolean canJump = true;
   float animation;
    Context context;
    Buttonn(Context context) {
        super(context);
        this.context=context;
        setBackgroundResource(R.drawable.canjumptemp);
        animation = context.getSharedPreferences("high",0).getInt("screenHeight",0)/3;
    }
    void change(Boolean c) {
        // do not jump
        if (c) {
            this.setBackgroundResource(R.drawable.cannotjumptemp);
            canJump = false;
        }
        // jump
        else {
            this.setBackgroundResource(R.drawable.canjumptemp);
            canJump = true;
        }
    }
    void move(){
        final ObjectAnimator jumpAni =ObjectAnimator.ofFloat(this,"translationY",0f,-animation);
        final ObjectAnimator landAni = ObjectAnimator.ofFloat(this,"translationY",-animation,0f);
        landAni.setInterpolator(new AccelerateInterpolator());
        jumpAni.setInterpolator(new DecelerateInterpolator());

        jumpAni.setDuration(350);
        landAni.setDuration(350);
        jumpAni.start();
        jumpAni.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    landAni.start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }

        );
    }
}
