package com.duyp.androidutils;

import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by duypham on 9/7/17.
 *
 */

public class StringUtils {

    /**
     * Get Basic Auth token by given username and password
     * @param username username
     * @param password password
     * @return {@link Base64} encoded auth token
     */
    public static String getBasicAuth(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    /**
     * Check empty Strings
     * @param strings input string array
     * @return true if all element is NOT empty
     */
    public static boolean isNotEmpty(String... strings) {
        for (String s : strings) {
            if (TextUtils.isEmpty(s)) {
                return false;
            }
        }
        return true;
    }
}
