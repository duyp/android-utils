package com.duyp.androidutils.image.glide.loader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.duyp.androidutils.functions.PlainConsumer;
import com.duyp.androidutils.image.glide.GlideOnCompleteListener;
import com.duyp.androidutils.image.glide.GlideUtils;
import com.duyp.androidutils.view.DimensionUtils;

import lombok.Setter;

/**
 * Created by duypham on 9/9/17.
 * Simple glide loader class for loading an image url or file into image view
 */

public class SimpleGlideLoader implements GlideLoader {

    protected static final float DEFAULT_THUMBNAIL_MULTIPLIER = 0.1f;
    protected static final int DEFAULT_THUMBNAIL_SIZE_DP = 120;

    protected RequestManager mRequestManager;

    protected Context mContext;

    /**
     * if true (default), loader will use fixed size thumbnail {@link GlideUtils#THUMBNAIL_SIZE}
     * else, loader will use size multiplier, default is 0.1f
     */
    @Setter
    protected boolean useFixedSizeThumbnail = false;

    @Setter
    protected float thumbnailMultiplier = DEFAULT_THUMBNAIL_MULTIPLIER;

    protected int thumbnailSizePx;

    @Setter
    @DrawableRes
    protected int placeHolderRes = -1;

    // deal with activity life circle
    public SimpleGlideLoader(Activity activity) {
        mRequestManager = Glide.with(activity);
        mContext = activity.getBaseContext();
        init();
    }

    // deal with fragment life circle
    public SimpleGlideLoader(Fragment fragment) {
        mRequestManager = Glide.with(fragment);
        mContext = fragment.getContext();
        init();
    }

    // normal context, if is instance of activity, glide with automatically deal with activity life circle cast from context
    public SimpleGlideLoader(Context context) {
        mRequestManager = Glide.with(context);
        mContext = context;
        init();
    }

    @CallSuper
    protected void init() {
        setThumbnailSizeDp(DEFAULT_THUMBNAIL_SIZE_DP);
    }

    public void setThumbnailSizeDp(int dp) {
        if (dp > 0) {
            thumbnailSizePx = DimensionUtils.dpToPx(mContext, dp);
        }
    }

    public <T> void loadImage(T source, ImageView imageView) {
        loadImage(source, imageView, null, null);
    }

    @Override
    public <T> void loadImage(T source, ImageView imageView, PlainConsumer<Boolean> completeConsumer) {
        loadImage(source, imageView, null, completeConsumer);
    }

    @Override
    public <T> void loadImage(T source, ImageView imageView, PlainConsumer<Boolean> thumbnailConsumer, PlainConsumer<Boolean> fullConsumer) {
        loadImageInternal(source, imageView, thumbnailConsumer, fullConsumer);
    }

    protected <T> void loadImageInternal(T source, ImageView imageView, @Nullable PlainConsumer<Boolean> thumbnailConsumer,
                                         @Nullable PlainConsumer<Boolean> fullConsumer) {
        createThumbnail(source, thumbnailConsumer)
                .listener(fullConsumer == null ? null : new GlideOnCompleteListener<T, GlideDrawable>() {
                    @Override
                    public void onCompleted(boolean success) {
                        fullConsumer.accept(success);
                    }
                })
                .into(imageView);
    }

    protected  <T> DrawableRequestBuilder<T> createThumbnail(T source, @Nullable PlainConsumer<Boolean> completeConsumer) {
        DrawableRequestBuilder<T> requestBuilder = GlideUtils.createFullRequestBuilder(mRequestManager, source);
        if (useFixedSizeThumbnail) {
            requestBuilder.thumbnail(GlideUtils.createThumbRequestBuilder(mRequestManager, source, thumbnailSizePx));
        } else {
            requestBuilder.thumbnail(thumbnailMultiplier);
        }
        if (placeHolderRes != -1) {
            requestBuilder.placeholder(placeHolderRes);
        }

        return requestBuilder.listener(completeConsumer == null ? null : new GlideOnCompleteListener<T, GlideDrawable>() {
            @Override
            public void onCompleted(boolean success) {
                completeConsumer.accept(success);
            }
        });
    }
}
