package com.duyp.app.image;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duyp.androidutils.image.glide.loader.GlideLoader;
import com.duyp.androidutils.image.glide.loader.TransitionGlideLoader;
import com.duyp.app.R;

import static com.duyp.app.image.GlideTestActivity.*;

public class ImageDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = findViewById(R.id.imv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(TRANSITION_NAME_IMAGE);
        }

        GlideLoader loader = new TransitionGlideLoader(this);
        loader.loadImage(URL, imageView);
    }
}
