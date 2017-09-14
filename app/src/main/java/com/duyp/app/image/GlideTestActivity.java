package com.duyp.app.image;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duyp.androidutils.image.glide.loader.GlideLoader;
import com.duyp.androidutils.image.glide.loader.SimpleGlideLoader;
import com.duyp.androidutils.navigator.ActivityNavigator;
import com.duyp.app.R;


public class GlideTestActivity extends AppCompatActivity {

    public static final String TRANSITION_NAME_IMAGE = "trans_image";

    public static final String URL = "https://imagejournal.org/wp-content/uploads/bb-plugin/cache/23466317216_b99485ba14_o-panorama.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        ActivityNavigator navigator = new ActivityNavigator(this);

        ImageView imageView = findViewById(R.id.imv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(TRANSITION_NAME_IMAGE);
        }

        GlideLoader loader = new SimpleGlideLoader(this);
        loader.loadImage(URL, imageView);

        imageView.setOnClickListener(view -> {
            navigator.startActivityWithTransition(ImageDetailActivity.class, intent -> {}, false, false, imageView);
        });

    }

}
