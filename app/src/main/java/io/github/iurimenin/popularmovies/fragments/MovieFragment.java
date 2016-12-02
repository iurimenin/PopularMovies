package io.github.iurimenin.popularmovies.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.popularmovies.BuildConfig;
import io.github.iurimenin.popularmovies.activity.DetailActivity;
import io.github.iurimenin.popularmovies.adapter.MovieAdapter;
import io.github.iurimenin.popularmovies.valueobject.MovieVO;
import io.github.iurimenin.popularmovies.R;
import io.github.iurimenin.popularmovies.Utils;

/**
 * Created by Iuri on 02/12/16.
 */
public class MovieFragment extends Fragment {

    @BindView(R.id.gridViewMovies) GridView mRridViewMovies;

    private MovieAdapter mMovieAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        mMovieAdapter = new MovieAdapter(getActivity(), new ArrayList<MovieVO>());
        mRridViewMovies.setAdapter(mMovieAdapter);
        mRridViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieVO movieVO = mMovieAdapter.getItem(i);
                Intent intent = new Intent(MovieFragment.this.getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, movieVO);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies () {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));

        ListMoviesTask fetchWeatherTask = new ListMoviesTask();
        fetchWeatherTask.execute(sortBy);
    }

    public class ListMoviesTask extends AsyncTask<String, Void, ArrayList<MovieVO>> {

        private final String LOG_TAG = ListMoviesTask.class.getSimpleName();
        private String json;

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

            if (result == null)
                return;
            mMovieAdapter.clear();
            mMovieAdapter.addAll(result);
        }
    }
}