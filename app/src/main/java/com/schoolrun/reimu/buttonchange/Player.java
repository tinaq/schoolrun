package com.schoolrun.reimu.buttonchange;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;



/**
 * Created by Reimu on 2016-02-28.
 */
public class Player extends ImageView {
    int y;
   int animation;
    String packageName;
    Buttonn button;
    int x;
    Context context;

    Player(Context context){
        super(context);
        this.context=context;

    }
    Player (Context context,Buttonn button){
        super(context);
        this.context=context;
        packageName = context.getPackageName();
        this.button=button;
        Drawable d= ResourcesCompat.getDrawable(getResources(), R.drawable.pinkrun_ani, null);
        this.setImageDrawable(d);
        animation = context.getSharedPreferences("high",0).getInt("screenHeight",0);
    }

    void doubleJump(){

    }
    void jump(){
        final SharedPreferences p = context.getSharedPreferences("high",0);
        String name = p.getString("selectedPlayer","pink");
       final  String run = name+"run_ani";
        String jump = name+"jump";
        final ObjectAnimator jumpAni =ObjectAnimator.ofFloat(this,"translationY",0f,-animation/3f);
        final ObjectAnimator landAni = ObjectAnimator.ofFloat(this, "translationY", -animation/3f, 0f);
        int id = this.getResources().getIdentifier(jump, "drawable", packageName);
        this.setImageDrawable(ResourcesCompat.getDrawable(getResources(), id, null));
        landAni.setInterpolator(new AccelerateInterpolator());
        jumpAni.setInterpolator(new DecelerateInterpolator());
        jumpAni.setDuration(250);
        landAni.setDuration(250);
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

                int id = Player.this.getResources().getIdentifier(run,"drawable", packageName);
                Player.this.setImageDrawable(ResourcesCompat.getDrawable(getResources(), id, null));
                ((AnimationDrawable) Player.this.getDrawable()).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    public VisualNovel getIntroVn(){

        int normal = R.drawable.pinkcover;
        int serious = R.drawable.pinksad;
        int happy = R.drawable.pinkhappy;
        final SharedPreferences p = context.getSharedPreferences("high",0);
        final SharedPreferences.Editor editor = p.edit();
        editor.putBoolean("pinkIntro",true).commit();

        final int l = 9;
        String[][] story = {{"Hello! My name is Noname, nice to meet you!\nI'm in grade 6 and it's almost summer time!", String.valueOf(normal)},
                {"It would be great if you can help me work through my last semester. I will tell you how to play the game",String.valueOf(normal)},
                {"Instructions are easy. When you see a green light, tap to jump over obstacles.\nThose are tedious homework that I can skip ",String.valueOf(happy)},
                {"But when it turns red, DO NOT jump over them!\nThose are homework that I actually need to do ;w;", String.valueOf(serious)},
                {"You can unlock stories and other characters once you achieve a certain score on a certain character",String.valueOf(normal)},
                {"Hold the characters at the character page and you will be able to see profiles of them,\nor what you need to unlock the character",String.valueOf(normal)},
                {"Other stuff is self-explanatory I think... \nI'm not good with words so I'll leave that for you to figure out",String.valueOf(serious)},
                {"Are you ready? ",String.valueOf(happy)},
                {"Oh, Noname is pronounced \"NO-NAH-MAY\" by the way", String.valueOf(serious)}};
        final VisualNovel vn = new VisualNovel(context, story, l);
        return vn;
    }
    public VisualNovel getEndVn(){

        String normal= String.valueOf(R.drawable.pinkcover);
        String sad = String.valueOf(R.drawable.pinksad);
        String happy = String.valueOf(R.drawable.pinkhappy);
        String[][] story = {{"I made it through the school year! Thank you so much!",happy},
                {"Now I can enjoy summer vacation and eat lots of ice cream every day!",happy},
                {"Um.. there will be a better CG for this when the artist isn't lazy",sad},
                {"I'll let you know when it updates!",normal},
                {"I'm going to go swimming now! See you around!",happy}};
        return new VisualNovel(context,story,story.length);
    }
    public VisualNovel getAdVn(){
        String sad = String.valueOf(R.drawable.pinksad);
        String[][] story = {{"Is..",sad},{"Is..is that an ad I see?",sad},
                {"Y-you'll forgive us, right?",sad},
                {"I'm sorry...",sad}};
        return new VisualNovel(context,story,story.length);
    }
    public String getInfo(){
        String story = "NAME: Noname \n\nAGE: 12 \n\nHEIGHT:145cm\n\nFAVOURITE FOOD: Strawberries \n\nFAVOURITE ANIMALS: Rabbits \n\nFAVOURITE COLOR:Pink\n\nHOBBIES: Jump rope" ;
        return story;
    }
    }

