package com.duyp.androidutils.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.AndroidRuntimeException;
import android.util.Log;

import com.duyp.androidutils.FileUtils;
import com.duyp.androidutils.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by duypham on 9/7/17.
 * Navigation utils functions
 */

public class NavigationUtils {

    /**
     * Open google map app with custom query
     * @param context Context
     * @param query google map query
     */
    public static void openGoogleMap(Context context, String query) {
        Uri gmmIntentUri = Uri.parse(query);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    /**
     * Open google map and point to specific address
     * @param context context
     * @param locationLatLng Lat and Long with format "lat,long"
     * @param address specific address
     */
    public static void openGoogleMap(Context context, String locationLatLng, String address){
        String query = context.getString(R.string.format_google_map_intent_uri, locationLatLng, locationLatLng, address);
        openGoogleMap(context, query);
    }

    /**
     * Open current application setting page
     * @param activity host activity
     */
    public static void openAppSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /**
     * Open system wifi setting
     * @param context context
     */
    public static void openWifiSetting(Context context) {
        Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
        try {
            context.startActivity(i);
        } catch (AndroidRuntimeException e) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    /**
     * Open gallery picker with specific type and request code
     * @param activity host activity
     * @param type type of result (e.g "image/*", "video/*")
     * @param requestCode request code for returned result
     */
    public static void openGalleryPickerForResult(Activity activity, String type, int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType(type);
        activity.startActivityForResult(photoPickerIntent, requestCode);
    }

    /**
     * Open gallery image pick
     * @param activity host activity
     * @param requestCode request code of returned result
     */
    public static void openGalleryImagePickerForResult(Activity activity, int requestCode) {
        openGalleryPickerForResult(activity, "image/*", requestCode);
    }

    /**
     * Open app 's play store page
     * @param context context
     */
    public static void openAppOnPlayStore(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * Open camera intent and wait for result image
     * The image result will be stored in return URI
     * @param activity host activity
     * @param requestCode request code
     * @return uri store result image, null value mean we can't create temp file for storing result,
     * or we might meet some exceptions, intent camera wo'nt be start as well.
     */
    @Nullable
    public static Uri openCameraIntentForResult(Activity activity, int requestCode) {

        // Check if there is a camera.
        PackageManager packageManager = activity.getPackageManager();
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return null;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile;
            try {
                photoFile = FileUtils.createTemporaryImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                return null;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                try {
                    Uri fileUri = FileUtils.getUriFromFile(activity, photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            fileUri);
                    activity.startActivityForResult(takePictureIntent, requestCode);
                    return fileUri;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
}
