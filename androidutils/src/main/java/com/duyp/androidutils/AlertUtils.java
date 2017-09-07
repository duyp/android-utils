package com.duyp.androidutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


/**
 */
public class AlertUtils {

    private static final String TAG = "AlertUtils";

    public static void showConfirmDialog(Activity activity, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_yes, positiveListener)
                .setNegativeButton(R.string.dialog_no, negativeListener)
                .show();
    }

    public static void showConfirmDialog(Activity activity, String title, String message, DialogInterface.OnClickListener listener) {
        if (TextUtils.isEmpty(title)) {
            title = "Confirm";
        }
        if (TextUtils.isEmpty(message)) {
            message = "Are you sure?";
        }
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_yes, listener)
                .setNegativeButton(R.string.dialog_no, null)
                .show();
    }

    public static void showConfirmDialogOne(Activity activity, String title, String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_yes, listener)
                .show();
    }

    public static void showAlertDialog(Context activity, String title, String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_ok, listener)
                .show();
    }

    public static void showAlertDialog(Context activity, String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_ok, listener)
                .show();
    }


    public static void showAlertDialog(Context context, String title, String message, String positive, String negative,
                                       DialogInterface.OnClickListener positiveListener,
                                       DialogInterface.OnClickListener negativeListener) {
        new AlertDialog.Builder(context)
                .setTitle(title).setMessage(message)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener)
                .setCancelable(false)
                .show();


    }

    public static void showToastLongMessage(Context context, String message) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showToastShortMessage(Context context, String message) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private static void showSnackBarInternal(View layout, String message,
                                             @Nullable String actionText,
                                             @Nullable View.OnClickListener actionClick,
                                             int duration) {
        if (layout != null && !TextUtils.isEmpty(message)) {
            CommonUtils.hideSoftKeyboard(layout.getContext());
            Snackbar snackbar = Snackbar.make(layout, message, duration);
            if (actionText != null && actionClick != null) {
                snackbar.setAction(actionText, actionClick);
            }
            snackbar.show();
        }
    }

    public static void showSnackBarLongMessage(View layout, String message, String actionText, View.OnClickListener actionClick) {
        showSnackBarInternal(layout, message, actionText, actionClick, Snackbar.LENGTH_LONG);
    }

    public static void showSnackBarShortMessage(View layout, String message, String actionText, View.OnClickListener actionClick) {
        showSnackBarInternal(layout, message, actionText, actionClick, Snackbar.LENGTH_SHORT);
    }

    public static void showSnackBarLongMessage(View layout, String message) {
        showSnackBarInternal(layout, message, null, null, Snackbar.LENGTH_LONG);
    }

    public static void showSnackBarShortMessage(View layout, String message) {
        showSnackBarInternal(layout, message, null, null, Snackbar.LENGTH_SHORT);
    }
}
