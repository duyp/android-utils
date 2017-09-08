package com.duyp.androidutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


/**
 * Utilities functions for android alert and snack bar showing
 */
public class AlertUtils {

    private static final String TAG = "AlertUtils";

    public static void showConfirmDialog(Context context, String message, DialogInterface.OnClickListener positiveListener) {
        showConfirmDialog(context, null, message, positiveListener, null);
    }

    public static void showConfirmDialog(Context context, String message, DialogInterface.OnClickListener positiveListener,
                                         DialogInterface.OnClickListener negativeListener) {
        showConfirmDialog(context, null, message, positiveListener, negativeListener);
    }

    public static void showConfirmDialog(Context context, @Nullable String title, String message,
                                         DialogInterface.OnClickListener positiveListener,
                                         @Nullable DialogInterface.OnClickListener negativeListener) {
        showAlertDialog(context, title, message, context.getString(R.string.dialog_yes), context.getString(R.string.dialog_no),
                positiveListener, negativeListener);
    }

    public static void showAlertDialog(Context context, String message) {
        showAlertDialog(context, null, message, null);
    }

    public static void showAlertDialog(Context context, String title, String message) {
        showAlertDialog(context, title, message, null);
    }

    public static void showAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        showAlertDialog(context, title, message, context.getString(R.string.dialog_ok), listener);
    }

    public static void showAlertDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        createAlertDialogBuilderInternal(context, message, context.getString(R.string.dialog_ok), listener)
                .show();
    }

    public static void showAlertDialog(Context context, @Nullable String title, String message,
                                       String positive, DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = createAlertDialogBuilderInternal(context, message, positive, listener);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.show();
    }

    public static void showAlertDialog(Context context, @Nullable String title, @NonNull String message,
                                       @NonNull String positive,
                                       @Nullable String negative,
                                       DialogInterface.OnClickListener positiveListener,
                                       DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = createAlertDialogBuilderInternal(context, message, positive, positiveListener);
        if (title != null) {
            builder.setTitle(title);
        }
        if (negative != null) {
            builder.setNegativeButton(negative, negativeListener);
        }
        builder.show();
    }

    private static AlertDialog.Builder createAlertDialogBuilderInternal(Context context,
                                                                        @NonNull String message,
                                                                        @NonNull String positiveButton,
                                                                        DialogInterface.OnClickListener positiveListener) {
        return new AlertDialog.Builder(context).setMessage(message)
                .setPositiveButton(positiveButton, positiveListener)
                .setCancelable(false);
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