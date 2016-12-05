package io.github.iurimenin.popularmovies.asynctask;

import android.content.Context;
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

/**
 * Created by Iuri on 05/12/16.
 */
public class ListMoviesTask extends AsyncTask<String, Void, ArrayList<MovieVO>> {

    private final String LOG_TAG = ListMoviesTask.class.getSimpleName();

    private String json;
    private AsyncTaskDelegate delegate;

    public ListMoviesTask(Context context, AsyncTaskDelegate responder){
        this.delegate = responder;
    }

    @Override
    protected ArrayList<MovieVO> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

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
            return getMoviesDataFromJson(json);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<MovieVO> getMoviesDataFromJson(String json) throws JSONException {

        final String RESULT = "results";

        JSONObject moviesJson = new JSONObject(json);
        JSONArray moviesArray = moviesJson.getJSONArray(RESULT);

        Type typeMovies = new TypeToken<List<MovieVO>>() {}.getType();
        ArrayList<MovieVO> moviesVO = new Gson().fromJson(moviesArray.toString(), typeMovies);

        return moviesVO;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieVO> result) {

        super.onPostExecute(result);
        if (delegate != null)
            delegate.processFinish(result);
    }
}
