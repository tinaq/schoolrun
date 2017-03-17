package com.schoolrun.reimu.buttonchange;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_PX;


/**
 * Created by Reimu on 2016-09-04.
 */

public class CharacterPage extends Activity {
     private Player p;
     private int h;
     private int w;
     private RelativeLayout r;
     private SharedPreferences pref;
    private String player;
    int id;
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.characterpage);
        pref = this.getSharedPreferences("high", 0);
        r = (RelativeLayout) findViewById(R.id.characterpage);
        id =((ViewGroup) r.getParent()).getId();
        LinearLayout buttonWrapper = (LinearLayout)findViewById(R.id.buttonWrapper);

        int width=pref.getInt("buttonWidth",0)/3;
        int height = pref.getInt("buttonHeight",0)/3*2;
        final GameButton intro = new GameButton(this,"other","INTRO");
        final ImageButton back = new ImageButton(this);
        back.setBackgroundResource(R.drawable.backbutton);


        RelativeLayout.LayoutParams backParams = new RelativeLayout.LayoutParams(height,width);
        back.setScaleType(ImageView.ScaleType.FIT_CENTER);
        final GameButton end = new GameButton(this,"other","STORY");
        w=pref.getInt("screenWidth",0);
        h=pref.getInt("screenHeight",0);

        player= pref.getString("selectedPlayer", "pink");
        ImageView play = (ImageView) findViewById(R.id.chara);
        play.setImageResource(getResources().getIdentifier(pref.getString("selectedPlayer", "pink").concat("large"), "drawable", getPackageName()));
        final RelativeLayout.LayoutParams im = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        im.setMargins(w/9, 0, 0, 0);
       play.setLayoutParams(im);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LargeFragment newFragment = new LargeFragment();
                FragmentManager ft = getFragmentManager();
                ft.beginTransaction().add(R.id.characterpage, newFragment).commit();
            }
        });
        p=GlobalMethodsSingleton.getPlayerByName(player,this);
        setInfo(p.getInfo());
        setListeners(intro, end, back);
        buttonWrapper.addView(intro);

        LinearLayout.LayoutParams wrapperParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       LinearLayout.LayoutParams endParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wrapperParams.leftMargin=w/50;
        wrapperParams.topMargin=h/20;
        endParams.leftMargin = w/30;
        buttonWrapper.addView(end,endParams);

        buttonWrapper.setLayoutParams(wrapperParams);
        r.addView(back,backParams);

    }

    private void startVn(boolean intro) {
        VnFragment vnf = new VnFragment();
        if (intro) vnf.vn = p.getIntroVn();
        if (!intro) vnf.vn = p.getEndVn();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().add(id, vnf, "vn").commit();
    }

    private void setListeners(Button intro, Button end, ImageButton back) {
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVn(true);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       UserGameData db = new UserGameData(CharacterPage.this);
                                       if(db.isEndUnlocked(player)) {
                                           startVn(false);
                                       }
                                       else{
                                           final GamePopup p = new GamePopup("OK","Score 200 with this \ncharacter to unlock!",CharacterPage.this);
                                           p.showAtLocation(r, 0, w / 4, h / 4);
                                       }
                                   }
                               }
        );
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CharacterPage.this, Select.class));
                finish();
            }
        });

    }
    private void setInfo(String info){
        LinearLayout textbox = (LinearLayout)findViewById(R.id.box);
        TextView infobox2 = (TextView)findViewById(R.id.info);
        RelativeLayout.LayoutParams param= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(w/30,0,0,0);
        param.setMargins(w/25*12,h/20,0,0);
        infobox2.setText(info);
        infobox2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Molot.otf"));
        infobox2.setLayoutParams(textParams);
        infobox2.setTextSize(COMPLEX_UNIT_PX,h/20);
        textbox.setLayoutParams(param);
        Log.d("fd","16");
    }

}
