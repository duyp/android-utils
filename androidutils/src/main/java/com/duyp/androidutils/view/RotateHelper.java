package com.duyp.androidutils.view;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by air on 3/2/17.
 *
 */

public class RotateHelper {

    private static final int DEFAULT_DURATION = 300;
    private static final LinearInterpolator interpolator = new LinearInterpolator();

    public static void rotateView(View v, float from, float to, @Nullable Animation.AnimationListener callBack) {
        RotateAnimation animation = new RotateAnimation(from, to,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setStartOffset(0);
        animation.setDuration(DEFAULT_DURATION);
        animation.setFillAfter(true);
        animation.setAnimationListener(callBack);
        animation.setInterpolator(interpolator);
        v.startAnimation(animation);
    }
}
