package com.duyp.androidutils.glide;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by duypham on 9/9/17.
 *
 */

public abstract class GlideOnCompleteListener<T, R> implements RequestListener<T, R> {

    @Override
    public boolean onException(Exception e, T model, Target<R> target, boolean isFirstResource) {
        onCompleted(false);
        return false;
    }

    @Override
    public boolean onResourceReady(R resource, T model, Target<R> target, boolean isFromMemoryCache, boolean isFirstResource) {
        onCompleted(true);
        return false;
    }

    public abstract void onCompleted(boolean success);
}
