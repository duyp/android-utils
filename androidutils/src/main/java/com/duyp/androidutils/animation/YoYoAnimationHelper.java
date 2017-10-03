package com.duyp.androidutils.animation;

import android.animation.Animator;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by phamd on 6/15/2017.
 * Helper class fof performing animation on views
 */

public class YoYoAnimationHelper {

    public static final Techniques[] TECHNIQUE_BOUNCES = new Techniques[] {Techniques.BounceIn, Techniques.BounceInDown, Techniques.BounceInLeft, Techniques.BounceInRight, Techniques.BounceInUp};
    public static final Techniques[] TECHNIQUE_SPECIALS = new Techniques[] {Techniques.Hinge, Techniques.RollIn, Techniques.RollOut, Techniques.Landing, Techniques.TakingOff, Techniques.DropOut};
    public static final Techniques[] TECHNIQUE_ZOOM = new Techniques[] {Techniques.ZoomIn, Techniques.ZoomOut, Techniques.ZoomInDown, Techniques.ZoomOutDown, Techniques.ZoomInLeft, Techniques.ZoomOutLeft,
            Techniques.ZoomInRight, Techniques.ZoomOutRight, Techniques.ZoomInUp, Techniques.ZoomOutUp};

    private static int current = 0;

    public static void fadeViewWithListener(final View target, boolean fadeIn, long duration){
        if (fadeIn) {
            fadeInViewWithListener(target, duration);
        } else {
            fadeOutViewWithListener(target, duration);
        }
    }

    public static void fadeInViewWithListener(final View target, long duration) {
        YoYo.with(Techniques.FadeIn)
                .duration(duration)
                .withListener(new SimpleAnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animator) {
                        target.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        target.setVisibility(View.VISIBLE);
                    }
                })
                .playOn(target);
    }

    public static void fadeInViews(long duration, final View... views) {
        for (View v : views) {
            fadeInViewWithListener(v, duration);
        }
    }

    public static void fadeOutViewWithListener(final View target, long duration) {
        if (target.getVisibility() == View.VISIBLE) {
            YoYo.with(Techniques.FadeOut)
                    .duration(duration)
                    .withListener(new SimpleAnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            target.setVisibility(View.INVISIBLE);
                        }
                    })
                    .playOn(target);
        }
    }

    public static void fadeOutViews(long duration, final View... views) {
        for (View v : views) {
            fadeOutViewWithListener(v, duration);
        }
    }

    public static void testAnimation(final View target, Techniques[] techniques) {
        if (current >= techniques.length) current = 0;
        YoYo.with(techniques[current++]).duration(AnimationConstants.DURATION_NORMAL).playOn(target);
    }
}
