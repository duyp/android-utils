package com.duyp.androidutils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class CustomSharedPreferences {
    /** KEY SHARE PREFERENCS **/

    private static final String PREFS_NAME = "CustomSharedPreferences_PREFS_NAME";

    /**
     * FEILD OF CLASS *
     */
    private String mPrefName = PREFS_NAME;
    private SharedPreferences mSharedPre;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    private static CustomSharedPreferences mInstance;

    public static CustomSharedPreferences getInstance(Context context, String prefName) {
        if (mInstance != null && !mInstance.mPrefName.equals(prefName)) {
            mInstance = null;
        }
        if (mInstance == null) {
            // IMPORTANCE: use getApplication context to prevent memory leak when store context instance as static field
            // Since application context is instance of application and lives as long as application
            // Thus store application context as static field is SAFE.
            mInstance = new CustomSharedPreferences(context.getApplicationContext(), prefName);
        }
        return mInstance;
    }

    public static CustomSharedPreferences getInstance(Context context) {
        return getInstance(context, PREFS_NAME);
    }

    @SuppressLint("CommitPrefEdits")
    public CustomSharedPreferences(final Context context, String prefName) {
        mContext = context;
        mPrefName = prefName;
        if (null != context) {
            if (mSharedPre == null) {
                mSharedPre = context.getSharedPreferences(mPrefName, 0);
            }

            mEditor = mSharedPre.edit();
        } else {
            mEditor = null;
            mSharedPre = null;
        }
    }

    public CustomSharedPreferences(final Context context) {
        this(context, PREFS_NAME);
    }

    @SuppressLint("CommitPrefEdits")
    private void refresh() {
        if (null != mContext) {
            mSharedPre = mContext.getSharedPreferences(mPrefName, 0);
            mEditor = mSharedPre.edit();
        } else {
            mEditor = null;
            mSharedPre = null;
        }
    }

    /**
     * Set data for String
     *
     * @param preName Preferences name
     * @param value   String input
     */
    public synchronized void setPreferences(final String preName,
                                                   final String value) {
        refresh();
        if (null != mEditor) {
            mEditor.putString(preName, value);
            mEditor.commit();
        }
    }

    /**
     * Get data for String
     *
     * @param preName      Preferences name
     * @param defaultValue
     * @return String or 0 if Name not existed
     */
    public String getPreferences(final String preName,
                                        final String defaultValue) {
        refresh();
        if (null != mSharedPre) {

            return mSharedPre.getString(preName, defaultValue);
        } else {

            return null;
        }
    }

    /**
     * Set data for boolean
     *
     * @param preName Preferences name
     * @param value   boolean input
     */
    public synchronized void setPreferences(final String preName,
                                                   final boolean value) {
        if (null != mEditor) {
            mEditor.putBoolean(preName, value);
            mEditor.commit();
        }
    }

    /**
     * Get data for boolean
     *
     * @param preName      Preferences name
     * @param defaultValue
     * @return boolean or 0 if Name not existed
     */
    public boolean getPreferences(final String preName,
                                         final boolean defaultValue) {
        if (null != mSharedPre) {
            return mSharedPre.getBoolean(preName, defaultValue);
        }

        return defaultValue;
    }

    /**
     * Set data for Integer
     *
     * @param preName Preferences name
     * @param value   Integer input
     */
    public synchronized void setPreferences(final String preName,
                                                   final int value) {
        if (null != mEditor) {
            mEditor.putInt(preName, value);
            mEditor.commit();
        }
    }

    /**
     * Get data for Integer
     *
     * @param preName      Preferences name
     * @param defaultValue
     * @return Integer or 0 if Name not existed
     */
    public int getPreferences(final String preName,
                                     final int defaultValue) {
        if (null != mSharedPre) {
            return mSharedPre.getInt(preName, defaultValue);
        } else {
            return 0;
        }
    }

    /**
     * Set data for Long
     *
     * @param preName Preferences name
     * @param value   Long input
     */
    public synchronized void setPreferences(final String preName,
                                                   final long value) {
        if (null != mEditor) {
            mEditor.putLong(preName, value);
            mEditor.commit();
        }
    }

    /**
     * Get data for Long
     *
     * @param preName      Preferences name
     * @param defaultValue
     * @return Long or 0 if Name not existed
     */
    public long getPreferences(final String preName,
                                      final long defaultValue) {
        if (null != mSharedPre) {
            return mSharedPre.getLong(preName, defaultValue);
        } else {
            return 0;
        }
    }

    /**
     * Set data for Float
     *
     * @param preName Preferences name
     * @param value   Float input
     */
    public synchronized void setPreferences(final String preName,
                                                   final float value) {
        if (null != mEditor) {
            mEditor.putFloat(preName, value);
            mEditor.commit();
        }
    }

    /**
     * Get data for Float
     *
     * @param preName      Preferences name
     * @param defaultValue
     * @return Float or 0 if Name not existed
     */
    public float getPreferences(final String preName,
                                       final float defaultValue) {
        if (null != mSharedPre) {
            return mSharedPre.getFloat(preName, defaultValue);
        } else {
            return 0;
        }

    }
}
