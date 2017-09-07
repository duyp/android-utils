package com.duyp.androidutils;

import android.util.Base64;

/**
 * Created by duypham on 9/7/17.
 */

public class StringUtils {

    public static String getBasicAuth(String username, String password) {
        String credentials = username + ":" + password;
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }
}
