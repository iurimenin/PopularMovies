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
import io.github.iurimenin.popularmovies.valueobject.VideoVO;

/**
 * Created by Iuri Menin on 26/12/16.
 */

public class ListMoviesVideosTask extends AsyncTask<String, Void, ArrayList<MovieVO>> {

    private final String LOG_TAG = ListMoviesVideosTask.class.getSimpleName();

    private final AsyncTaskDelegate delegate;
    private final ArrayList<MovieVO> listMovies;

    public ListMoviesVideosTask(ArrayList<MovieVO> listMovies, AsyncTaskDelegate responder) {
        this.delegate = responder;
        this.listMovies = listMovies;
    }

    @Override
    protected ArrayList<MovieVO> doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        for (MovieVO vo: listMovies) {

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
                        return null;
                    }
                    json = buffer.toString();

                    vo.setVideos(getVideoDataFromJson(json));
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (JSONException e) {
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
        }

        return listMovies;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieVO> movieVOs) {
        super.onPostExecute(movieVOs);
        if (delegate != null)
            delegate.processFinish(movieVOs);
    }

    private ArrayList<VideoVO> getVideoDataFromJson(String json) throws JSONException {

        final String RESULT = "results";

        JSONObject videosJson = new JSONObject(json);
        JSONArray videosArray = videosJson.getJSONArray(RESULT);

        Type typeMovies = new TypeToken<List<VideoVO>>() {}.getType();
        ArrayList<VideoVO> videosVO = new Gson().fromJson(videosArray.toString(), typeMovies);

        return videosVO;
    }
}
