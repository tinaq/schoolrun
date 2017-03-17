package com.schoolrun.reimu.buttonchange;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 * Created by Reimu on 2016-09-10.
 */

public class UserGameData extends SQLiteOpenHelper {
    public static final String DB_NAME ="userdata.db";
    public static final int DB_VER = 1;
    private static UserGameData database = null;
    private final LinkedList<String> names = new LinkedList<String>(Arrays.asList("pink","yellow","violin","tba","tba2"));



    public UserGameData(Context context) {
        super(context,DB_NAME, null,DB_VER);
    }
    public static UserGameData getInstance(Context context){
        if(database==null){
            database= new UserGameData(context);
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = ("CREATE TABLE IF NOT EXISTS GameData (ID INTEGER PRIMARY KEY, Character TEXT NOT NULL DEFAULT pink,Unlocked INTEGER NOT NULL DEFAULT 0, IntroVn BOOLEAN NOT NULL DEFAULT false, EndVn BOOLEAN NOT NULL DEFAULT false," +
                "UnlockVN INTEGER NOT NULL DEFAULT 200, HighScore int not null DEFAULT 0,Unlocks text ,UnlockCondition INTEGER NOT NULL DEFAULT 50)");
        db.execSQL(create);
        init(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    private void init(SQLiteDatabase db) {
        ListIterator<String> li = names.listIterator();
        while(li.hasNext()) {
            String name = li.next();
            if (!li.hasNext()) {
                db.execSQL("INSERT INTO GameData (Character,Unlocks) VALUES ('" + name + "','')");
            } else {
                String unlocks = li.next();
                li.previous();
                db.execSQL("INSERT INTO GameData (Character,Unlocks) VALUES ('" + name + "'," + "'" + unlocks + "')");
            }
        }
        db.execSQL("UPDATE GameData SET Unlocked = 1 WHERE Character = 'pink'");

    }

    public boolean canUnlockCharacterAndUpdate(String name,int score) {
        SQLiteDatabase db=getWritableDatabase();
        Cursor c =db.rawQuery("SELECT * FROM GameData WHERE Character=?",new String[]{name});
        c.moveToFirst();
        int indexUnlockCond = c.getColumnIndex("UnlockCondition");
        String unlocks = getUnlocks(name);
        int primKey = Integer.valueOf(c.getString(c.getColumnIndex("ID")))+1;
        int cond = Integer.valueOf(c.getString(indexUnlockCond));
        c.close();
        if(score>=cond&&!isCharacterUnlocked(unlocks)){
            String update = ("UPDATE GameData SET Unlocked=1 WHERE ID=" + primKey);
            db.execSQL(update);
            return true;
        }
       return false;
    }
    public void addHighScore(String name, int score){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c = db.rawQuery("SELECT HighScore FROM GameData WHERE Character=?",new String[]{name});
        c.moveToFirst();
        int high = c.getInt(0);
        c.close();
        if(score>high){
            db.execSQL("UPDATE GameData SET HighScore="+high+" WHERE Character='"+name+"'");
        }
    }
    public String getUnlocks(String name){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT Unlocks FROM GameData WHERE Character=?",new String[]{name});
        c.moveToFirst();
        String unlocked = c.getString(0);
        c.close();
        return unlocked;
    }
    public boolean isEndUnlocked(String name){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT EndVn FROM GameData WHERE Character=?" ,new String[]{name});
        c.moveToFirst();
        Log.i("faddd",Integer.toString(c.getInt(0)));
        boolean unlocked = c.getInt(0)==1;
        c.close();
        return unlocked;
    }

    public boolean canUnlockEndAndUpdate(String name,int score) {
        if(score>=200) {
            SQLiteDatabase db = getWritableDatabase();
            Cursor update = db.rawQuery("SELECT EndVn FROM GameData WHERE Character=?" ,new String[]{name});
            update.moveToFirst();

            int unlocked = update.getInt(0);
            update.close();
            if(unlocked==0){
                db.execSQL("UPDATE GameData SET EndVn = 1 WHERE Character ='"+name+"'");
                return true;
            }
        }
        return false;
    }

    public boolean isCharacterUnlocked(String name) {
        SQLiteDatabase db=getWritableDatabase();
        Cursor c= db.query("GameData", new String[]{"Unlocked"}, "Character=?", new String[]{name}, null, null, null);
        c.moveToFirst();
        int i = c.getInt(0);
        c.close();
        return i==1;
    }
    public String[] getCharacters(){
        SQLiteDatabase db=getWritableDatabase();
        Cursor c= db.query("GameData",new String[]{"Character"},null,null,null,null,null);
        String[] name = new String[c.getCount()];
        c.moveToFirst();
        name[0]=c.getString(0);
        int i = 1;
        while(c.moveToNext()){
            name[i]=c.getString(0);
            i++;
        }
        c.close();
        return name;
    }
    public boolean hasPlayedIntroAndUpdate(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query("GameData", new String[]{"IntroVn","ID"}, "Character=?", new String[]{name}, null, null, null);
        c.moveToFirst();
        int result = c.getInt(0);
        int id =c.getInt(1);

        if(result==0){
            db.execSQL("UPDATE GameData SET IntroVn = 1 WHERE ID= "+id);
        }
        c.close();
        return result==1;
    }
}