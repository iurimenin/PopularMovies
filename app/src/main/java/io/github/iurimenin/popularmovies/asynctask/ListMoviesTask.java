package io.github.iurimenin.popularmovies.asynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.github.iurimenin.popularmovies.Utils;
import io.github.iurimenin.popularmovies.interfaces.AsyncTaskDelegate;
import io.github.iurimenin.popularmovies.valueobject.MovieVO;
import io.github.iurimenin.popularmovies.valueobject.ReviewVO;
import io.github.iurimenin.popularmovies.valueobject.VideoVO;

/**
 * Created by Iuri on 05/12/16.
 */
public class ListMoviesTask extends AsyncTask<String, Void, ArrayList<MovieVO>> {

    private final String LOG_TAG = ListMoviesTask.class.getSimpleName();
    private final String RESULT = "results";

    private String json;
    private AsyncTaskDelegate delegate;

    private HttpURLConnection urlConnection = null;
    private BufferedReader reader = null;

    public ListMoviesTask(AsyncTaskDelegate responder){
        this.delegate = responder;
    }

    @Override
    protected ArrayList<MovieVO> doInBackground(String... params) {

        try {

            Uri uri = Uri.parse(Utils.MOVIES_API_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(Utils.API_KEY, Utils.MY_MOVIE_BD_API_KEY)
                    .build();

            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                json = buffer.toString();
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            ArrayList<MovieVO> movieVOs = getMoviesDataFromJson(json);
            this.getYouTubeKeyForMovie(movieVOs);
            this.getReviewsForMovie(movieVOs);
            return movieVOs;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<MovieVO> getMoviesDataFromJson(String json) throws JSONException {

        JSONObject moviesJson = new JSONObject(json);
        JSONArray moviesArray = moviesJson.getJSONArray(RESULT);

        Type typeMovies = new TypeToken<List<MovieVO>>() {}.getType();
        ArrayList<MovieVO> moviesVO = new Gson().fromJson(moviesArray.toString(), typeMovies);

        return moviesVO;
    }

    private void getYouTubeKeyForMovie (ArrayList<MovieVO> movieVOList){
        for (MovieVO vo: movieVOList) {

            String json;
            try {

                Uri uri = Uri.parse(Utils.MOVIES_API_URL).buildUpon()
                        .appendPath(vo.getId())
                        .appendPath(Utils.VIDEOS)
                        .appendQueryParameter(Utils.API_KEY, Utils.MY_MOVIE_BD_API_KEY)
                        .build();

                URL url = new URL(uri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        return;
                    }
                    json = buffer.toString();

                    vo.setVideos(getVideoDataFromJson(json));
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }
    }

    private ArrayList<VideoVO> getVideoDataFromJson(String json) throws JSONException {

        JSONObject videosJson = new JSONObject(json);
        JSONArray videosArray = videosJson.getJSONArray(RESULT);

        Type typeMovies = new TypeToken<List<VideoVO>>() {}.getType();
        ArrayList<VideoVO> videosVO = new Gson().fromJson(videosArray.toString(), typeMovies);

        return videosVO;
    }

    private void getReviewsForMovie(ArrayList<MovieVO> movieVOs) {

        for (MovieVO vo: movieVOs) {

            String json;
            try {

                Uri uri = Uri.parse(Utils.MOVIES_API_URL).buildUpon()
                        .appendPath(vo.getId())
                        .appendPath(Utils.REVIEWS)
                        .appendQueryParameter(Utils.API_KEY, Utils.MY_MOVIE_BD_API_KEY)
                        .build();

                URL url = new URL(uri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        return;
                    }
                    json = buffer.toString();

                    vo.setReviews(getReviewDataFromJson(json));
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }
    }

    private ArrayList<ReviewVO> getReviewDataFromJson(String json) throws JSONException {

        JSONObject reviewJson = new JSONObject(json);
        JSONArray reviewArray = reviewJson.getJSONArray(RESULT);

        Type typeMovies = new TypeToken<List<ReviewVO>>() {}.getType();
        ArrayList<ReviewVO> reviewVOs = new Gson().fromJson(reviewArray.toString(), typeMovies);

        return reviewVOs;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieVO> result) {

        super.onPostExecute(result);
        if (delegate != null)
            delegate.processFinish(result);
    }
}
