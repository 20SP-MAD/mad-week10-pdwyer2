package edu.lewisu.cs.example.courserating;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Settings {

    public static int DEFAULT_RATING = 0;
    public static int DEFAULT_THEME = 0;

    public static void setPreferences(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        Boolean darkTheme = preferences.getBoolean(activity.getResources().getString(R.string.pref_theme_key), false);
        if (darkTheme) {
            DEFAULT_THEME = R.style.AppThemeDark;
        } else {
            DEFAULT_THEME = R.style.AppTheme;
        }

        String ratingString = preferences.getString(activity.getString(R.string.pref_rating_key), "0");
        Settings.DEFAULT_RATING = Integer.parseInt(ratingString);
    }
}
