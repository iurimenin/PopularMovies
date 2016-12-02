package io.github.iurimenin.popularmovies;

import android.content.Context;
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
    public static final String MY_MOVIE_BD_API_KEY = "YourAPIKeyHere";

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
}
