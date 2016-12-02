package io.github.iurimenin.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Iuri on 02/12/16.
 */
public class MovieFragment extends Fragment {

    @BindView(R.id.gridViewMovies) GridView mRridViewMovies;

    private ArrayAdapter<String> mForecastAdapter;

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

        mRridViewMovies.setAdapter(new ImageAdapter(this.getContext()));
       /* mRridViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String forecast = mForecastAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });*/
        return rootView;
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

    public class ListMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = ListMoviesTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {

            if (result == null)
                return;
            mForecastAdapter.clear();
            mForecastAdapter.addAll(result);
        }
    }
}