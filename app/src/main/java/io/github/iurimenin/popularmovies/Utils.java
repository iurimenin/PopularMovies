package io.github.iurimenin.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Iuri on 02/12/16.
 */

public class Utils {

    public static final String API_KEY = "api_key";
    public static final String MOVIES_API_URL = "http://api.themoviedb.org/3/movie/";
    public static final String MY_MOVIE_BD_API_KEY = "MY_API_KEY";
    public static final String VIDEOS = "videos";
    public static final String REVIEWS = "reviews";

    private static final String IMAGES_URL_500 = "/w500";
    private static final String IMAGES_URL_185 = "/w185";
    private static final String IMAGES_URL = "https://image.tmdb.org/t/p/";

    public static String getImageUrl780(String poster_path) {

        return IMAGES_URL + IMAGES_URL_500 + poster_path;
    }

    public static String getImageUrl185(String poster_path) {

        return IMAGES_URL + IMAGES_URL_185 + poster_path;
    }

    public static String convertDate(Context context, String releaseDate) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(releaseDate);
            return DateUtils.formatDateTime(context, date.getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return releaseDate;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static String getSortPreference(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_popular));
    }

    public static boolean isFavoriteMovie(Context context, String id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(id, false);
    }

    public static void markAsFavorite(Context context, String id, boolean b) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(id, b);
        editor.apply();
    }
}
