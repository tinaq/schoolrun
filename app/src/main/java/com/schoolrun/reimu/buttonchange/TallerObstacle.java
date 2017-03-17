package com.schoolrun.reimu.buttonchange;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

/**
 * Created by Reimu on 2016-10-10.
 */

public class TallerObstacle extends Obstacle {
    TallerObstacle(Context context) {
        super(context);
        Drawable d= ResourcesCompat.getDrawable(getResources(), R.drawable.tallerobstacle, null);
        setImageDrawable(d);
    }
}
