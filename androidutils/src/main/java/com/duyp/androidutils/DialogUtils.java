package com.duyp.androidutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;


/**
 */
public class DialogUtils {

    private static final String TAG = "DialogUtils";

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
}
