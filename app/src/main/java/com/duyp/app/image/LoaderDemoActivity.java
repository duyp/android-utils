package com.duyp.app.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.duyp.androidutils.image.glide.GlideUtils;
import com.duyp.app.R;

/**
 * Created by duypham on 9/13/17.
 */

public class LoaderDemoActivity extends AppCompatActivity {

    public static final String URL = "https://www.nasa.gov/sites/default/files/thumbnails/image/oct_1_2015_flare.jpg";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_loader);
        ImageView imv = findViewById(R.id.imvImage);

        Glide.with(this)
                .load(URL)
                .thumbnail(0.1f)
//                .thumbnail(GlideUtils.createThumbRequestBuilder(Glide.with(this), URL, 50)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        e.printStackTrace();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Log.d("image", "THUMB: isMemoryCache = " + isFromMemoryCache + ", isFirstResource: " + isFirstResource);
//                        return false;
//                    }
//                    })
//                )
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.d("image", "FULL: false ");
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("image", "FULL: isMemoryCache = " + isFromMemoryCache + ", isFirstResource: " + isFirstResource);
                        return false;
                    }
                    })
                .into(imv);
    }
}
