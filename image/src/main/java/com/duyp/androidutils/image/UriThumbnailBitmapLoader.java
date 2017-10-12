package com.duyp.androidutils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by phamd on 10/12/2017.
 *
 */

public class UriThumbnailBitmapLoader extends UriBitmapLoader {

    public UriThumbnailBitmapLoader(Context context, Uri source) {
        super(context, source);
        maxSize = 100;
    }

    @Override
    protected Bitmap load(Uri source) {
        return BitmapUtils.decodeSampledBitmap(mContext, source, maxSize, maxSize).bitmap;
    }
}
