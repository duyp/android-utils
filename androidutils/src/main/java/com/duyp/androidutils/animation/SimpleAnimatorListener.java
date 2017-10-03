package com.duyp.androidutils.animation;

import android.animation.Animator;

/**
 * Created by phamd on 6/22/2017.
 * Simple implement of animator listener
 */

public abstract class SimpleAnimatorListener implements Animator.AnimatorListener {

    private PlainConsumer<Animator> onStart;
    private PlainConsumer<Animator> onEnd;
    private PlainConsumer<Animator> onRepeat;

    public SimpleAnimatorListener onStart(PlainConsumer<Animator> onStart) {
        this.onStart = onStart;
        return this;
    }

    public SimpleAnimatorListener onEnd(PlainConsumer<Animator> onEnd) {
        this.onEnd = onEnd;
        return this;
    }

    public SimpleAnimatorListener onRepeat(PlainConsumer<Animator> onRepeat) {
        this.onRepeat = onRepeat;
        return this;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (onStart != null) {
            onStart.accept(animation);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (onEnd != null) {
            onEnd.accept(animation);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        onAnimationEnd(animation);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        if (onRepeat != null) {
            onRepeat.accept(animation);
        }
    }
}
