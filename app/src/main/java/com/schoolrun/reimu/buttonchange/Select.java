package com.schoolrun.reimu.buttonchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;

/**
 * Created by Reimu on 2016-06-01.
 */
public class Select extends AppCompatActivity {
        private HashMap<String,ImageView> charaImages = new HashMap<>();
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        final RelativeLayout main = (RelativeLayout) findViewById(R.id.menu);
        pref = this.getSharedPreferences("high", 0);
        final SharedPreferences.Editor editor = pref.edit();
        int h=pref.getInt("screenHeight",0);
        UserGameData db= UserGameData.getInstance(this);
        final String[] selectCharacter = db.getCharacters();
        final HorizontalScrollView r = (HorizontalScrollView) findViewById(R.id.select);
        LinearLayout sel = (LinearLayout)findViewById(R.id.selectChar);
        int scrollviewHeight=h/5*4;
        r.getLayoutParams().height=scrollviewHeight;
        final GameButton back = new GameButton(this,StringResources.BACK);
        final Button start = new GameButton(this,StringResources.START);
        RelativeLayout.LayoutParams backParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        backParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        RelativeLayout.LayoutParams startParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        startParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        startParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        back.setLayoutParams(backParams);
        start.setLayoutParams(startParams);
        main.addView(back);
        main.addView(start);
        final HorizontalScrollView l = (HorizontalScrollView) findViewById(R.id.select);
        for (final String name : selectCharacter) {
            LinearLayout.LayoutParams r2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final ImageView iv = new ImageView(this);
            String lock = "unlocked";
            if (db.isCharacterUnlocked(name)) {
                String key = name.concat(lock);
                iv.setImageResource(getResources().getIdentifier(key, "drawable", getPackageName()));
                iv.setBackgroundResource(R.drawable.selectborder);
                if (!name.equals(pref.getString("selectedPlayer", "pink"))) {
                    iv.setBackgroundResource(0);
                }
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            for(String n:selectCharacter){
                                if (!name.equals(pref.getString("selectedPlayer", "pink"))) {
                                    editor.putString("selectedPlayer", name);
                                    iv.setBackgroundResource(R.drawable.selectborder);
                                    editor.commit();
                                    removeBorders();
                                }
                            }
                        }
                });
                iv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        editor.putString("selectedPlayer", name);
                        editor.commit();
                        startActivity(new Intent(Select.this, CharacterPage.class));
                        return true;
                    }
                });
            } else if(name.contains("tba")) {
                iv.setImageResource(R.drawable.tba);
            }
            else{
                iv.setImageResource(R.drawable.locked);
            }
            iv.setScaleType(ImageView.ScaleType.FIT_START);
            iv.setAdjustViewBounds(true);
            r2.setMargins(scrollviewHeight / 20, 10, 0, scrollviewHeight / 9);
            iv.setLayoutParams(r2);
            sel.addView(iv);
            l.setMinimumHeight(scrollviewHeight);
            charaImages.put(name,iv);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Select.this,StartMenu.class));
            }
        });
    }
    private void removeBorders(){
        String cur = pref.getString("selectedPlayer","pink");
        for(String name: charaImages.keySet()){
            if(!name.equals(cur)){
                charaImages.get(name).setBackgroundResource(0);
            }
        }
    }
}
