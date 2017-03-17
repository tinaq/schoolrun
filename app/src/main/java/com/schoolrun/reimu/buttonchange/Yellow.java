package com.schoolrun.reimu.buttonchange;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.util.Random;

/**
 * Created by Reimu on 2016-06-01.
 */
public class Yellow extends Player {
    Context context;
    Yellow(Context context) {
        super(context);
        this.context = context;
        this.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.yellowrun_ani, null));
    }
    Yellow(Context context, Buttonn button) {
        super(context, button);
        this.context =context;
        this.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.yellowrun_ani, null));

    }
    public void doubleJump(){
        final ObjectAnimator jumpAni =ObjectAnimator.ofFloat(this,"translationY",0f,(-animation-80)/2f);
        final ObjectAnimator landAni = ObjectAnimator.ofFloat(this, "translationY", (-animation-80)/2f, 0f);
        this.setImageResource(R.drawable.yellowjump);
        landAni.setInterpolator(new AccelerateInterpolator());
        jumpAni.setInterpolator(new DecelerateInterpolator());
        jumpAni.setDuration(300);
        landAni.setDuration(300);
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
        landAni.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (Yellow.this.button.canJump) {
                    Random r = new Random();
                    int x = r.nextInt(4);
                    if (button.canJump) {
                        if (x == 0) {
                            button.change(true);
                        } else {
                            button.change(false);
                        }
                    }
                }

                Yellow.this.setImageResource(R.drawable.yellowrun_ani);
                ((AnimationDrawable) Yellow.this.getDrawable()).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    public VisualNovel getIntroVn (){
        int normal = R.drawable.yellowcover;
        int serious = R.drawable.yellowsad;
        int happy = R.drawable.yellowhappy;
        final SharedPreferences p = context.getSharedPreferences("high",0);
        final SharedPreferences.Editor editor = p.edit();
        final int l = 6;
        String[][] story = {{"Hi there! I'm Elise, a 2nd year student in XXX Girl's High School",String.valueOf(normal)},
                {"I really want to go shopping with friends...But here I am at school...",String.valueOf(serious)},
                {"Anyways, game play! You can long-press to jump higher now!",String.valueOf(normal)},
                {"Help me get through this semester and I will get you a giant teddy bear!",String.valueOf(happy)},
                {"You don't like teddy bears?",String.valueOf(serious)},
                {"You don't like teddy bears?\nToo bad because I like them",String.valueOf(normal)}
        };
                    editor.putBoolean("yellowIntro",true);
                    editor.commit();
        final VisualNovel vn = new VisualNovel(context, story, l);
        return vn;
    }
    public VisualNovel getEndVn() {
        String normal = Integer.toString(R.drawable.yellowcover);
        String happy = Integer.toString(R.drawable.yellowhappy);
        String sweat = Integer.toString(R.drawable.yellowsad);
        String[][] story={{"Ehhh school's finally over..So tired..",normal},
                {"I passed my classes and that's all what matters, right?",happy},
                {"As promised, let's go teddy bear shopping!",happy},
                {"Oh...this pink plaid teddy bear is so cute!",normal},
                {"Oh...this pink plaid teddy bear is so cute!\nThis one too!",happy},
                {"Oops, I ended up getting 10 teddy bears for myself..",sweat},
                {"Here here take this one with the red bowtie!",normal},
                {"Here here take this one with the red bowtie!\nI think it really suits you!",happy}};
        return new VisualNovel(context,story,8);
    }
    public String getInfo(){
        String story = "NAME: Elise\n\nAGE: 17 \n\nHEIGHT: 165cm \n\nFAVOURITE FOOD: Cake \n\nFAVOURITE ANIMALS: (Teddy) bears \n\nFAVOURITE COLOR:Red\n\nHOBBIES: Texting" ;
        return story;
    }


    }

