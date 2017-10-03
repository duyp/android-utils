package com.duyp.androidutils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;


public class AspectLockedImageView extends AppCompatImageView {
    private float aspectRatio = 0;
    private AspectRatioSource aspectRatioSource = null;

    public AspectLockedImageView(Context context) {
        super(context);
    }

    public AspectLockedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectLockedImageView);
        aspectRatio = a.getFloat(R.styleable.AspectLockedImageView_AspectLockedImageView_ratio, 0);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float localRatio = aspectRatio;

        if (localRatio == 0.0 && aspectRatioSource != null && aspectRatioSource.getHeight() > 0) {
            localRatio = (float) aspectRatioSource.getWidth() / (float) aspectRatioSource.getHeight();
        }

        if (localRatio == 0.0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int lockedWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            int lockedHeight = View.MeasureSpec.getSize(heightMeasureSpec);

            if (lockedWidth == 0 && lockedHeight == 0) {
                throw new IllegalArgumentException("Both width and height cannot be 0");
            }

            int hPadding = getPaddingLeft() + getPaddingRight();
            int vPadding = getPaddingTop() + getPaddingBottom();

            lockedWidth -= hPadding;
            lockedHeight -= vPadding;

            if (lockedHeight > 0 && (lockedWidth > lockedHeight * localRatio)) {
                lockedWidth = (int) (lockedHeight * localRatio + .5);
            } else {
                lockedHeight = (int) (lockedWidth / localRatio + .5);
            }

            // Add the padding of the border.
            lockedWidth += hPadding;
            lockedHeight += vPadding;

            // Ask children to follow the new preview dimension.
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(lockedWidth, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(lockedHeight, View.MeasureSpec.EXACTLY));
        }
    }

    public void setAspectRatioSource(View view) {
        this.aspectRatioSource = new ViewAspectRatioSource(view);
    }

    public void setAspectRatioSource(AspectRatioSource aspectRatioSource) {
        this.aspectRatioSource = aspectRatioSource;
    }

    public void setAspectRatio(float aspectRatio) {
        if (aspectRatio <= 0.0) {
            throw new IllegalArgumentException("aspect ratio must be positive");
        }

        if (this.aspectRatio != aspectRatio) {
            this.aspectRatio = aspectRatio;
            requestLayout();
        }
    }

    public interface AspectRatioSource {
        int getWidth();

        int getHeight();
    }

    public static class ViewAspectRatioSource implements AspectRatioSource {

        private View v = null;

        public ViewAspectRatioSource(View v) {
            this.v = v;
        }

        @Override
        public int getWidth() {
            return v.getWidth();
        }

        @Override
        public int getHeight() {
            return v.getHeight();
        }
    }
}
