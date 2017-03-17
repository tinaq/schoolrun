package com.schoolrun.reimu.buttonchange;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.animation.AccelerateInterpolator;

import java.util.Random;

/**
 * Created by kqian on 10/12/16.
 */

public class Violin extends Player {
    int animation;
    Violin(Context context) {
        super(context);
    }
    Violin(Context context,Buttonn button) {
        super(context, button);
        animation = context.getSharedPreferences("high",0).getInt("screenWidth",0);
        this.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.violinrun_ani, null));
    }

    public Buttonn shoot(){
        Buttonn b = new Buttonn(getContext());
        final  String run = "violinrun_ani";
        String jump = "violinjump";
        final ObjectAnimator shootAni =ObjectAnimator.ofFloat(b,"translationX",0f,animation);
        int id = this.getResources().getIdentifier(jump, "drawable", packageName);
        this.setImageDrawable(ResourcesCompat.getDrawable(getResources(), id, null));
        shootAni.setInterpolator(new AccelerateInterpolator());
        shootAni.setDuration(300);
        shootAni.start();
        shootAni.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (Violin.this.button.canJump) {
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
                                    int id = Violin.this.getResources().getIdentifier(run,"drawable", packageName);
                                    Violin.this.setImageDrawable(ResourcesCompat.getDrawable(getResources(), id, null));
                                    ((AnimationDrawable) Violin.this.getDrawable()).start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }
        );
        return b;
    }
    public VisualNovel getIntroVn(){
        String  normal= Integer.toString(R.drawable.violincover);
        String happy =Integer.toString( R.drawable.violinhappy);
        String sad =Integer.toString( R.drawable.violinsad);
        String wunai = Integer.toString(R.drawable.violinwunai);
        String[][] story = {{"...Sorry I didn't notice that you were here.",normal},{"...Sorry I didn't notice that you were here.\nI'm Volya Lebedev",normal},
                {"...",normal},{"...\nNo I am not a girl",wunai},{"...\nNo I am not a girl\n...No this isn't a violin",wunai},
                {"I play the viola..",normal},{"...",wunai},{"...\nI'm used to this already, it's fine",wunai},
                {"Since you're playing this game, it means you're bored, right?",normal},
                {"You will help me with my practice, right?",happy},
                {"I'm doing sight-reading for this piece, so I might read the notes wrong.",wunai},
                {"Please help me so that I play the right notes. When the light is red, don't play the note",normal},
                {"Thank you for your time... I would play something for you for helping me,\nif you don't mind",normal},
                {"But sadly not much people like classic music anymore.",wunai},
                {"But sadly not much people like classic music anymore.\nMaybe I can give you ticket's to my twin brother's band..",sad}};
        return new VisualNovel(context,story,story.length);
        }
    public VisualNovel getEndVn(){

        String  normal= Integer.toString(R.drawable.violincover);
        String happy =Integer.toString( R.drawable.violinhappy);
        String sad =Integer.toString( R.drawable.violinsad);
        String wunai = Integer.toString(R.drawable.violinwunai);
        int l = 1;
        String[][] story = {{"Thank you for helping me...",happy},
                {"Do you think you can give classical music a chance after hearing me play the viola?",normal},
                {"Oh.. I don't know if you play any instruments or not yet",normal},
                {"Oh.. I don't know if you play any instruments or not yet,\nIf you do, please don't give it up",sad},
                {"Promise me you'll give this a listen though!",normal},
                {"Dvorak's New World.",normal},
                {"Dvorak's New World.\nIt's one of my favourite pieces",normal}};
        return new VisualNovel(context,story,story.length);
    }
    public String getInfo(){
        String story = "NAME: Volya Lebedev \n\nAGE: 16\n\nHEIGHT:170cm\n\nFAVOURITE FOOD: Pudding \n\nFAVOURITE ANIMAL: Swans\n\nFAVOURITE COLOR:White\n\nHOBBIES: Viola" ;
        return story;
    }
    }

