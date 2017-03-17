package com.schoolrun.reimu.buttonchange;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by kqian on 12/12/16.
 */

public class AdManager {
    static InterstitialAd mInterstitialAd;
    static Context context;
    AdManager(){

    }
    public static void load(Context context){

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-9876383615346337/5481192402");
        requestNewInterstitial();
    }

    private static void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        try {
            mInterstitialAd.loadAd(adRequest);
        }
        catch(NullPointerException e){

        }
    }
    public static InterstitialAd getAd(){
        if(mInterstitialAd==null){
            load(context);
        }
        return mInterstitialAd;
    }


}
