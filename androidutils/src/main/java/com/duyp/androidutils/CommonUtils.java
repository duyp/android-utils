package com.duyp.androidutils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by duypham on 9/7/17.
 * Common utilities
 */

public class CommonUtils {

    /**
     * Get activity from given context
     * @param context context
     * @return activity if given context or its base context is instance of activity, null if not
     */
    public static Activity getActivityFromContext(@NonNull Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
    
    public static Point getScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new Point (dm.widthPixels, dm.heightPixels);
    }

    public static int getStatusBarHeight(Context activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    public static void hideSoftKeyboard(Context context) {
        try {
            Activity activity = getActivityFromContext(context);
            if (activity != null) {
                hideSoftKeyboard(activity);
            }
        } catch (Exception ignored) {}
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            // Check if no view has focus:
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception ignored) {}
    }

    public static void showSoftKeyboard(Context context, EditText edt) {
        edt.setEnabled(true);
        edt.setFocusable(true);
        edt.setFocusableInTouchMode(true);
        edt.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT);

        edt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
        edt.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void changeStatusBarColor(Context activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity) activity).getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(color);
        }
    }

    /**
     * get key hash to use facebook sdk
     * @param activity host activity
     * @return key hash
     */
    public static String getKeHash(Activity activity) {
        String keyHash = "";
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash", keyHash);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        } catch (NoSuchAlgorithmException ignored) {
        }
        return keyHash;
    }


}
