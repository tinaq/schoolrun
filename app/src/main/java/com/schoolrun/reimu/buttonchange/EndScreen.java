package com.schoolrun.reimu.buttonchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * Created by Reimu on 2016-05-16.
 */
public class EndScreen extends AppCompatActivity{
    int textColor ;
    PopupWindow p;
    Typeface custom_font;
    RelativeLayout end;
    int h;
    int w;
    String curChara;
    UserGameData db;
    int score;
    SharedPreferences pref;
    ImageView player;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textColor =  ResourcesCompat.getColor(getResources(), R.color.textColor, null);
        setContentView(R.layout.endscreen);
         end = (RelativeLayout)findViewById(R.id.end);
       pref = getSharedPreferences("high", 0);
        final SharedPreferences.Editor editor = pref.edit();
        curChara = pref.getString("selectedPlayer","pink");
        final int hs = (pref.getInt("high",0));
        score=pref.getInt("current",0);
        h = pref.getInt("screenHeight",0);
        w = pref.getInt("screenWidth",0);
        editor.putInt("money",score);
         db = UserGameData.getInstance(this);
        db.addHighScore(curChara,hs);
        //textview things
        final TextView finalScore = new TextView(this);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/Molot.otf");

        RelativeLayout.LayoutParams finalS = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        finalS.setMargins(w * 48 / 100, h / 8, w / 20, 0);
        finalScore.setText("High: " + Integer.toString(hs) + "\n\n" + "Score: " + Integer.toString(score)+ "\n\n"+"$$ earned: " + Integer.toString(score));
        finalScore.setTextSize(COMPLEX_UNIT_PX,h/12);
        finalScore.setTypeface(custom_font);
        finalScore.setTextColor(textColor);



        end.addView(finalScore,finalS);
        addButtons();
       player = new ImageView(this);

        RelativeLayout.LayoutParams playerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        playerParams.setMargins(w / 65, h / 15, w / 5 * 3, 0);
        player.setLayoutParams(playerParams);
        player.setScaleType(ImageView.ScaleType.FIT_END);
        end.addView(player,playerParams);

        if(!pref.getBoolean("hasPlayedAdVn",false)&&pref.getBoolean("playedAd",false)){
            showAdVn();
            editor.putBoolean("hasPlayedAdVn",true);
            editor.commit();
        }
        else if(db.canUnlockEndAndUpdate(curChara,score)) {
            showEndVn();
            player.setImageResource(getResources().getIdentifier(pref.getString("selectedPlayer", "pink").concat("happy"), "drawable", getPackageName()));
        }
        else {
            showPopup();
            player.setImageResource(getResources().getIdentifier(pref.getString("selectedPlayer", "pink").concat("sad"), "drawable", getPackageName()));

        }


    }
    protected void onDestroy()
    {super.onDestroy();
    }
    protected void addButtons(){
        final Button select =new GameButton(this,"other","HOME");
        final Button retry = new GameButton(this,"start","TRY AGAIN");
        RelativeLayout.LayoutParams finalB = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        finalB.setMargins(w *47/100, h *4/5, 0, 0);
        RelativeLayout.LayoutParams selectParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        selectParams.setMargins(w / 4*3, h*4/5, 0, 0);
        select.setLayoutParams(selectParams);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EndScreen.this, StartMenu.class));
                finish();
            }
        });
        end.addView(select);
        end.addView(retry, finalB);
    }
    public void showPopup() {
        if (db.canUnlockCharacterAndUpdate(curChara, score)) {
            String message = null;
            if (db.getUnlocks(curChara).contains("tba")) {
                message = "You unlocked a character!...that will come soon.\n Please be patient! (๑′ᴗ‵๑)";
            } else {
                message = "New Character(s) Unlocked!";
            }
            p = new GamePopup("OK", message, EndScreen.this);
            end.post(new Runnable() {
                @Override
                public void run() {
                    p.showAtLocation(end, Gravity.CENTER, 0, 0);
                }
            });
        }
    }
    public void showEndVn(){
            VnFragment vn = new VnFragment();
            vn.vn = GlobalMethodsSingleton.getPlayerByName(curChara, EndScreen.this).getEndVn();
            int id = ((ViewGroup) end.getParent()).getId();
            EndScreen.this.getFragmentManager().beginTransaction().add(id, vn, "vn").commit();


    }
    private void showAdVn(){
        if(db.canUnlockEndAndUpdate(curChara,score)) {
            showEndVn();
            player.setImageResource(getResources().getIdentifier(pref.getString("selectedPlayer", "pink").concat("happy"), "drawable", getPackageName()));
        }
        else{
            player.setImageResource(getResources().getIdentifier(pref.getString("selectedPlayer", "pink").concat("sad"), "drawable", getPackageName()));
        }
        Player p = new Player(this);
        VisualNovel vn = p.getAdVn();
        VnFragment vnFragment = new VnFragment();
        vnFragment.vn = vn;
        int id = ((ViewGroup)end.getParent()).getId();
        EndScreen.this.getFragmentManager().beginTransaction().add(id,vnFragment,"vn" ).commit();


    }
}

