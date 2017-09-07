package com.duyp.androidutils.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.duyp.androidutils.R;

/**
 * Created by duypham on 9/7/17.
 * Navigation utils functions
 */

public class NavigationUtils {

    public static void openGoogleMap(Context context, String query) {
        Uri gmmIntentUri = Uri.parse(query);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public static void openGoogleMap(Context context, String locationLatLng, String address){
        String query = context.getString(R.string.format_google_map_intent_uri, locationLatLng, locationLatLng, address);
        openGoogleMap(context, query);
    }

    public static void openAppSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }
}
