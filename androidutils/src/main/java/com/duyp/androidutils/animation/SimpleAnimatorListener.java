package com.duyp.androidutils.animation;

import android.animation.Animator;

/**
 * Created by phamd on 6/22/2017.
 * Simple implement of animator listener
 */

public class SimpleAnimatorListener implements Animator.AnimatorListener {

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {

    }

    @Override
    public void onAnimationCancel(Animator animator) {
        onAnimationEnd(animator);
    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
