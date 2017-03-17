package com.schoolrun.reimu.buttonchange;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Reimu on 2016-08-25.
 */

public class VnFragment extends Fragment{
    VisualNovel vn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return vn;
    }
    @Override
    public void onPause(){
        super.onPause();
        Context context = getActivity();
        if(context instanceof EndScreen){
            ((EndScreen) context).showPopup();
        }
    }
}
