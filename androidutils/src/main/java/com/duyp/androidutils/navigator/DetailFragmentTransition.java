package com.duyp.androidutils.navigator;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DetailFragmentTransition extends TransitionSet {
    public DetailFragmentTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds())
                .addTransition(new ChangeTransform())
                .addTransition(new ChangeImageTransform())
                .addTransition(new ChangeClipBounds());
    }
}