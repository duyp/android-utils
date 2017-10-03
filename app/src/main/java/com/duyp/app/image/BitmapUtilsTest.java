package com.duyp.app.image;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.duyp.androidutils.image.BitmapUtils;
import com.duyp.androidutils.navigation.NavigationUtils;
import com.duyp.app.BasePermissionActivity;
import com.duyp.app.R;

/**
 * Created by duypham on 10/3/17.
 *
 */

public class BitmapUtilsTest extends BasePermissionActivity{

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_loader);

        requestPermission(() -> {
            imageView = findViewById(R.id.imvImage);
            imageView.setOnClickListener(view -> {
                NavigationUtils.openGalleryImagePickerForResult(this, 12);
            });
        }, true, this::finish, Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.READ_EXTERNAL_STORAGE);
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
