package com.fenghuo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

public class ActivityUtil {
    private ActivityUtil() {
    }

    public static void setFullscreen(Activity activity) {
        activity.getWindow().setFlags(1024, 1024);
    }

    public static void setNoTitle(Activity activity) {
        activity.requestWindowFeature(1);
    }

    public static DisplayMetrics getDisplayInfo(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static void savePreference(Context context, String str, String str2) {
        Editor edit = context.getSharedPreferences(NotificationParameter.packageName, 0).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static String getPreference(Context context, String str, String str2) {
        return context.getSharedPreferences(NotificationParameter.packageName, 0).getString(str, str2);
    }

    public static void setInputMethodClose(Context context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
            if (inputMethodManager.isActive()) {
                inputMethodManager.toggleSoftInput(1, 2);
            }
        } catch (Exception e) {
        }
    }

    public static boolean isInputMethodActive(Context context) {
        try {
            return ((InputMethodManager) context.getSystemService("input_method")).isActive();
        } catch (Exception e) {
            return false;
        }
    }

    public static String getSoftwareVersion(Context context) {
        String str = "";
        try {
            return context.getPackageManager().getPackageInfo(NotificationParameter.packageName, 16384).versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }
}
