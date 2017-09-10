package com.duyp.androidutils.image.glide.loader;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.duyp.androidutils.image.glide.GlideOnCompleteListener;
import com.duyp.androidutils.image.glide.GlideUtils;

/**
 * Created by duypham on 9/9/17.
 * {@link GlideLoader} that support for better shared element transition in destination transition page
 * In start page which contains source image, we can use normal loader by using {@link SimpleGlideLoader}
 * but need to set {@link SimpleGlideLoader#setUseFixedSizeThumbnail(boolean)} as true value
 */

public class TransitionGlideLoader extends SimpleGlideLoader {

    public TransitionGlideLoader(Activity activity) {
        super(activity);
    }

    public TransitionGlideLoader(Fragment fragment) {
        super(fragment);
    }

    public TransitionGlideLoader(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();
        // ensure we 'r using fixed thumbnail size
        setUseFixedSizeThumbnail(true);
    }

    /**
     * Support better for loading image at detail page
     * Firstly, we load the image from cache only,
     * if image is'nt cached, perform normal loading with thumbnail place holder
     * @param source image source
     * @param imageView destination image view
     * @param listener complete listener
     */
    @Override
    public <T> void loadImage(T source, ImageView imageView, GlideOnCompleteListener<T, GlideDrawable> listener) {
        GlideUtils.createFullNoNetworkRequestBuilder(mRequestManager, source)
                .listener(new GlideOnCompleteListener<T, GlideDrawable>() {
                    @Override
                    public void onCompleted(boolean success) {
                        if (success && listener != null) {
                            listener.onCompleted(true);
                        }
                        if (!success) {
                            // perform normal loading
                            createThumbnail(source).listener(listener).into(imageView);
                        }
                    }
                }).into(imageView);
    }
}
