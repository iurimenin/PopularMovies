package io.github.iurimenin.popularmovies;

/**
 * Created by Iuri on 02/12/16.
 */

public class Utils {

    public static final String IMAGES_URL = "https://image.tmdb.org/t/p/w500/";
    public static final String MOVIES_URL = "https://api.themoviedb.org/3/discover/movie?";
    public static final String SORT_BY = "sort_by=";
    public static final String SORTY_BY_VOTE = "vote_average";
    public static final String SORTY_BY_POPULARITY = "popularity";
    public static final String ASC = ".asc";
    public static final String API_KEY = "&api_key=";

    //https://api.themoviedb.org/3/discover/movie?sort_by=popularity.asc&api_key=67fccec8dbc33668cc830348ade95787
    //http://api.themoviedb.org/3/discover/movie?sort_by=&api_key=67fccec8dbc33668cc830348ade95787
}
