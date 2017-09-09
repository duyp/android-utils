package com.duyp.androidutils.image.glide;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.Px;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.duyp.androidutils.functions.PlainConsumer;
import com.duyp.androidutils.image.glide.loader.GlideNetworkDisablingLoader;

/**
 * Created by duypham on 9/8/17.
 * Glide Utils class to deal with image loading operations, support for smooth shared element transition
 */

public final class GlideUtils {

    public static final int THUMBNAIL_SIZE = 256;
    public static final float THUMBNAIL_MULTIPLIER = 0.1f;

    // not allow create instance of this class
    private GlideUtils() {}

    /**
     * Load image from given url, placeholder with thumbnail (size multiplier = 0.1f)
     * @param context context
     * @param source image source
     * @param imageView destination image
     */
    public static <T> void loadImage(Context context, T source, ImageView imageView) {
        loadImage(context, source, imageView, THUMBNAIL_MULTIPLIER);
    }

    /**
     * Load image from given url, placeholder with thumbnail by given size multiplier
     * @param context context
     * @param source image source
     * @param sizeMultiplier thumbnail size multiplier
     * @param imageView destination image
     */
    public static <T> void loadImage(Context context, T source, ImageView imageView, float sizeMultiplier) {
        createFullRequestBuilder(context, source)
                .thumbnail(sizeMultiplier)
                .into(imageView);
    }

    /**
     * Load full image from given url without thumbnail placeholder
     * @param context context
     * @param source image source
     * @param imageView destination image
     */
    public static <T> void loadImageNoThumbnail(Context context, T source, ImageView imageView) {
        createFullRequestBuilder(context, source).into(imageView);
    }

    public static <T> void loadImageDrawableResource(Context context, @DrawableRes int drawableRes, ImageView imageView) {
        createFullRequestBuilder(context, null)
                .placeholder(drawableRes)
                .into(imageView);
    }

    /**
     * Load thumbnail image from given url
     * @param context context
     * @param source image source
     * @param imageView destination image
     */
    public static <T> void loadImageThumbnail(Context context, T source, ImageView imageView) {
        loadImageThumbnail(context, source, imageView, THUMBNAIL_SIZE);
    }

    /**
     * Load thumbnail image from given url
     * @param context context
     * @param source image source
     * @param imageView destination image
     * @param thumbSizePx thumbnail size
     */
    public static <T> void loadImageThumbnail(Context context, T source, ImageView imageView, @Px int thumbSizePx) {
        loadImageThumbnail(Glide.with(context), source, imageView, thumbSizePx);
    }

    /**
     * Load thumbnail image from given url with default thumbnail size
     * @param requestManager Glide Request manager
     * @param source image source
     * @param imageView destination image
     */
    public static <T> void loadImageThumbnail(RequestManager requestManager, T source, ImageView imageView) {
        loadImageThumbnail(requestManager, source, imageView, THUMBNAIL_SIZE);
    }

    /**
     * Load thumbnail image from given url with specify thumbnail size
     * @param requestManager Glide Request manager
     * @param source image source
     * @param imageView destination image
     */
    public static <T> void loadImageThumbnail(RequestManager requestManager, T source, ImageView imageView, @Px int thumbSizePx) {
        createThumbRequestBuilder(requestManager, source, thumbSizePx)
                .into(imageView);
    }

    /**
     * Load image bitmap from image source with full size
     * @param context context
     * @param source image source
     * @param consumer bitmap result consummer
     * @param <T> type of image source
     */
    public static <T> void loadImageBitmap(Context context, T source, PlainConsumer<Bitmap> consumer) {
        Glide.with(context).load(source)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        consumer.accept(resource);
                    }
                });
    }

    /**
     * Load image bitmap from image source
     * @param context context
     * @param source image source
     * @param outSizeW output bitmap width, -1 for original size
     * @param outSizeH output bitmap height, -1 for original size
     * @param consumer bitmap result consummer
     * @param <T> type of image source
     */
    public static <T> void loadImageBitmap(Context context, T source, @Px int outSizeW, @Px int outSizeH, PlainConsumer<Bitmap> consumer) {
        Glide.with(context).load(source)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(outSizeW, outSizeH) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        consumer.accept(resource);
                    }
                });
    }

    /**
     * Create a request builder to load full image from image source
     * @param requestManager request manager
     * @param source image source (url for file, uri..)
     * @param <T> type or source (String, File, Uri..)
     * @return instance of {@link DrawableRequestBuilder}
     */
    public static<T> DrawableRequestBuilder<T> createFullRequestBuilder(RequestManager requestManager, T source) {
        return requestManager
                .load(source)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    /**
     * Create a request builder to load cached image only (no network)
     * @param requestManager request manager
     * @param source image source (url for file, uri..)
     * @param <T> type or source (String, File, Uri..)
     * @return instance of {@link DrawableRequestBuilder}
     */
    public static <T> DrawableRequestBuilder<T> createFullNoNetworkRequestBuilder(RequestManager requestManager, T source) {
        return requestManager
                .using(new GlideNetworkDisablingLoader<T>())
                .load(source)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    /**
     * Create a request builder to load thumbnail image (with default specific size to deal with shared element transitions)
     * @param requestManager request manager
     * @param source image source (url for file, uri..)
     * @param <T> type or source (String, File, Uri..)
     * @return instance of {@link DrawableRequestBuilder}
     */
    public static <T> DrawableRequestBuilder<T> createThumbRequestBuilder(RequestManager requestManager, T source) {
        return createThumbRequestBuilder(requestManager, source, THUMBNAIL_SIZE);
    }

    /**
     * Create a request builder to load thumbnail image by given thumbnail size in pixel
     * @param requestManager request manager
     * @param source image source (url for file, uri..)
     * @param <T> type or source (String, File, Uri..)
     * @return instance of {@link DrawableRequestBuilder}
     */
    public static <T> DrawableRequestBuilder<T> createThumbRequestBuilder(RequestManager requestManager, T source, @Px int thumbSizePx) {
        return requestManager
                .load(source)
                .override(thumbSizePx, thumbSizePx)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    /**
     * Create a request builder to load full image from image source
     * @param context context
     * @param source image source (url for file, uri..)
     * @param <T> type or source (String, File, Uri..)
     * @return instance of {@link DrawableRequestBuilder}
     */
    public static<T> DrawableRequestBuilder<T> createFullRequestBuilder(Context context, T source) {
        return createFullRequestBuilder(Glide.with(context), source);
    }

    /**
     * Create a request builder to load cached image only (no network)
     * @param context context
     * @param source image source (url for file, uri..)
     * @param <T> type or source (String, File, Uri..)
     * @return instance of {@link DrawableRequestBuilder}
     */
    public static <T> DrawableRequestBuilder<T> createFullNoNetworkRequestBuilder(Context context, T source) {
        return createFullNoNetworkRequestBuilder(Glide.with(context), source);
    }

    /**
     * Create a request builder to load thumbnail image (with default specific size to deal with shared element transitions)
     * @param context context
     * @param source image source (url for file, uri..)
     * @param <T> type or source (String, File, Uri..)
     * @return instance of {@link DrawableRequestBuilder}
     */
    public static <T> DrawableRequestBuilder<T> createThumbRequestBuilder(Context context, T source) {
        return createThumbRequestBuilder(Glide.with(context), source);
    }

    /**
     * Create a request builder to load thumbnail image by given thumbnail size in pixel
     * @param context context
     * @param source image source (url for file, uri..)
     * @param <T> type or source (String, File, Uri..)
     * @return instance of {@link DrawableRequestBuilder}
     */
    public static <T> DrawableRequestBuilder<T> createThumbRequestBuilder(Context context, T source, @Px int thumbSizePx) {
        return createThumbRequestBuilder(Glide.with(context), source, thumbSizePx);
    }
}
