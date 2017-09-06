package com.duyp.androidutils.animation;

import android.view.animation.AccelerateInterpolator;

/**
 * Created by duypham on 8/20/17.
 */

public class CustomAccelerateInterpolator extends AccelerateInterpolator{

    private final float mFactor;

    public CustomAccelerateInterpolator(float factor) {
        mFactor = factor;
    }

    @Override
    public float getInterpolation(float input) {
        // y = ax^2
        return mFactor * (float)Math.pow(input, 2);
    }
}
