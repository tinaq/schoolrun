package com.schoolrun.reimu.buttonchange;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kqian on 10/20/16.
 */

public class GlobalMethodsSingleton{

    private static Map<String,Player> playerMap = new HashMap<String, Player>();
    private static Map<String,Integer> playerHeightMap = new HashMap<String, Integer>();

    private static Context context;

    private GlobalMethodsSingleton() {

    }
    private static void init(){
        if(playerMap.isEmpty()) {
            playerMap.put("pink",new Player(context,new Buttonn(context)));
            playerMap.put("yellow",new Yellow(context,new Buttonn(context)));
            playerMap.put("violin",new Violin(context,new Buttonn(context)));
        }
    }
    private static void initHeightMap(int h) {
        if(playerHeightMap.isEmpty()) {
            playerHeightMap.put("pink",h*11/20);
            playerHeightMap.put("yellow",h/2);
            playerHeightMap.put("violin",h/2);
        }
    }


    protected static Player getPlayerByName(String name,Context c){
        context=c;
        init();
        Player p = playerMap.get(name);
        p.context = context;
        return p;
    }
    protected static int getPlayerY(String name, Context c){
        int h = c.getSharedPreferences("high",0).getInt("screenHeight",0);
        initHeightMap(h);
        return playerHeightMap.get(name);
    }
}
