package com.schoolrun.reimu.buttonchange;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by Reimu on 2016-11-04.
 */

public class OnStartActivityClickListener implements View.OnClickListener {
    Context context;
    int viewId;
  OnStartActivityClickListener(Context context){
      this.context=context;
  }
    @Override
    public void onClick(View v) {
        String name = context.getSharedPreferences("high", 0).getString("selectedPlayer", "pink");
        StringResources.startGame=true;
        UserGameData db = UserGameData.getInstance(context);

        if (!db.hasPlayedIntroAndUpdate(name)) {
            Player p = GlobalMethodsSingleton.getPlayerByName(name, context);
            final VisualNovel vn = p.getIntroVn();
            VnFragment vnf = new VnFragment();
            vnf.vn = vn;
             int id= (((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content)).getId();

            FragmentTransaction ft = ((Activity) context).getFragmentManager().beginTransaction();
            ft.add(id, vnf, "vn");
            ft.commit();

        } else {
           startGame(name);
        }
    }
    public void startGame(String name){
        StringResources.startGame=false;
        if (name.equals("pink") || name.equals("yellow")) {
            context.startActivity(new Intent(context, MainActivity.class));
        } else if (name.equals("violin")) {
            context.startActivity(new Intent(context, ViolinGameplay.class));
        }
        ((Activity)context).finish();
    }

}
