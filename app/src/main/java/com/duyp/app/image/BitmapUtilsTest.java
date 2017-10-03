package com.duyp.app.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.duyp.androidutils.image.BitmapUtils;
import com.duyp.androidutils.navigator.NavigationUtils;
import com.duyp.app.R;

/**
 * Created by duypham on 10/3/17.
 *
 */

public class BitmapUtilsTest extends AppCompatActivity{

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_loader);

        imageView = findViewById(R.id.imvImage);

        imageView.setOnClickListener(view -> {
            NavigationUtils.openGalleryImagePickerForResult(this, 12);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Bitmap bm = BitmapUtils.decodeUriToScaledBitmap(this, imageUri, 0, 1500, 1500);
                imageView.setImageBitmap(bm);
            }
        }
    }
}
