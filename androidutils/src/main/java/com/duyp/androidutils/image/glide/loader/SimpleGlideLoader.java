package com.duyp.androidutils.image.glide.loader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.duyp.androidutils.image.PlainConsumer;
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
        loadImage(source, imageView, null);
    }

    @Override
    public <T> void loadImage(T source, ImageView imageView, PlainConsumer<Boolean> completeConsumer) {
        loadImageInternal(source, imageView, completeConsumer);
    }

    protected <T> void loadImageInternal(T source, ImageView imageView, @Nullable PlainConsumer<Boolean> fullConsumer) {
        createFullWithThumbnail(source, null)
                .listener(fullConsumer == null ? null : new GlideOnCompleteListener<T, GlideDrawable>() {
                    @Override
                    public void onCompleted(boolean success) {
                        fullConsumer.accept(success);
                    }
                })
                .into(imageView);
    }

    /**
     * Create request builder to load image with thumbnail
     * @param source image source
     * @param completeConsumer consume complete state, true if success
     * @param <T> type of source
     * @return request builder
     */
    protected  <T> DrawableRequestBuilder<T> createFullWithThumbnail(T source, @Nullable PlainConsumer<Boolean> completeConsumer) {
        DrawableRequestBuilder<T> requestBuilder = GlideUtils.createRequestBuilder(mRequestManager, source);
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

    /**
     * Create request builder to load image full image without thumbnail
     * @param source image source
     * @param completeConsumer consume complete state, true if success
     * @param <T> type of source
     * @return request builder
     */
    protected <T> DrawableRequestBuilder<T> createFullNoThumbnail(T source, @Nullable PlainConsumer<Boolean> completeConsumer) {
        return GlideUtils.createRequestBuilder(mRequestManager, source)
                .listener(completeConsumer == null ? null : new GlideOnCompleteListener<T, GlideDrawable>() {
                    @Override
                    public void onCompleted(boolean success) {
                        completeConsumer.accept(success);
                    }
                });
    }

    @Override
    public <T> void loadImage(T source, @NonNull ImageView imv, @NonNull ProgressBar pb, @NonNull View reloadView) {
        pb.setVisibility(View.VISIBLE);
        reloadView.setVisibility(View.GONE);
        loadImage(source, imv, success -> {
            pb.setVisibility(View.GONE);
            if (!success) {
                reloadView.setVisibility(View.VISIBLE);
                if (!reloadView.hasOnClickListeners()) {
                    reloadView.setOnClickListener(view -> {
                        loadImage(source, imv, pb, reloadView);
                    });
                }
            } else {
                reloadView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public <T> void loadImage(T original, T thumb, @NonNull ImageView imv, @NonNull ProgressBar pb, @NonNull View reloadView) {
        pb.setVisibility(View.VISIBLE);
        reloadView.setVisibility(View.GONE);
        loadImageWithCache(original, thumb, imv, success -> {
            pb.setVisibility(View.GONE);
            if (!success) {
                reloadView.setVisibility(View.VISIBLE);
                if (!reloadView.hasOnClickListeners()) {
                    reloadView.setOnClickListener(view -> {
                        loadImage(original, thumb, imv, pb, reloadView);
                    });
                }
            } else {
                reloadView.setVisibility(View.GONE);
            }
        });
    }

    public <T> void loadImageWithCache(T original, T thumb, ImageView imageView, PlainConsumer<Boolean> successConsumer) {
        GlideUtils.createNoNetworkRequestBuilder(mRequestManager, original)
                .listener(new GlideOnCompleteListener<T, GlideDrawable>() {
                    @Override
                    public void onCompleted(boolean success) {
                        if (success) {
                            successConsumer.accept(true);
                        } else {
                            loadImageNoCache(original, thumb, imageView, successConsumer);
                        }
                    }
                }).into(imageView);
    }

    public <T> void loadImageNoCache(T original, T thumb, ImageView imageView, PlainConsumer<Boolean> successConsumer) {
        createFullNoThumbnail(thumb, success -> {
            createFullNoThumbnail(original, successConsumer).into(imageView);
        }).into(imageView);
    }
}
