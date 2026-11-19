package com.gute.gastrovote;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHandler {
    private static final String PREF_NAME = "com.gute.Gastrovote.PREFS";
    private static final String PREF_USER_NAME = "username";
    private static final String BAR_NUMBER_VALUE = "bar_count";
    private static final String DONE_BARS_VALUE = "done_bars";

    private static SharedPreferences getPrefs(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUsername(Context context, String username) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(PREF_USER_NAME, username);
        editor.apply();
    }
    public static String getUsername(Context context) {
        return getPrefs(context).getString(PREF_USER_NAME, "user");
    }

    public static void saveProgress(Context context, int doneBars, int totalBars) {

        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt(BAR_NUMBER_VALUE, totalBars);
        editor.putInt(DONE_BARS_VALUE, doneBars);
        editor.apply();
    }
    public static void saveProgress(Context context, int doneBars) {

        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt(DONE_BARS_VALUE, doneBars);
        editor.apply();
    }
    public static int getProgress(Context context) {
        return getPrefs(context).getInt(DONE_BARS_VALUE, 0);
    }

    public static int getTotalBars(Context context) {
        return getPrefs(context).getInt(BAR_NUMBER_VALUE, 0);
    }
}
