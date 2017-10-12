package com.duyp.androidutils.image.glide.loader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.duyp.androidutils.image.glide.GlideOnCompleteListener;
import com.duyp.androidutils.image.glide.GlideUtils;
import com.duyp.androidutils.rx.functions.PlainConsumer;

/**
 * Created by duypham on 9/9/17.
 * {@link GlideLoader} that support better for shared element transitions in destination page
 * In start page (List, RecyclerView,...) which contains source image, we can use normal loader by using {@link SimpleGlideLoader}
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
        // setUseFixedSizeThumbnail(true); // temporary disable fixed size thumbnail
    }

    /**
     * Support better for loading image at detail page with shared element transitions
     * Firstly, we load the image from cache only,
     * if image is'nt cached, perform normal loading with thumbnail placeholder
     */
    @Override
    public <T> void loadImage(T source, ImageView imageView,
                              @Nullable PlainConsumer<Boolean> fullConsumer) {
        GlideUtils.createNoNetworkRequestBuilder(mRequestManager, source)
                .listener(new GlideOnCompleteListener<T, GlideDrawable>() {
                    @Override
                    public void onCompleted(boolean success) {
                        if (success && fullConsumer != null) {
                            fullConsumer.accept(true);
                        } else if (!success) {
                            // perform normal loading
                            loadImageInternal(source, imageView, fullConsumer);
                        }
                    }
                }).into(imageView);
    }
}