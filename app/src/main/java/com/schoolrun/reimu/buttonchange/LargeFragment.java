package com.schoolrun.reimu.buttonchange;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Reimu on 2016-09-06.
 */

public class LargeFragment extends Fragment {
    Button back;
    int h2;
    boolean large = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.large, container, false);
        final ImageView p = (ImageView) view.findViewById(R.id.large);
        final SharedPreferences pref = getActivity().getSharedPreferences("high", 0);
        final int w = pref.getInt("screenWidth", 0);
        p.setImageResource(getResources().getIdentifier(pref.getString("selectedPlayer", "pink").concat("large"), "drawable", view.getContext().getPackageName()));
        p.setRotation(270f);
        p.getLayoutParams().height = w;
        p.setScaleType(ImageView.ScaleType.FIT_CENTER);
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().beginTransaction().remove(LargeFragment.this).commit();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
