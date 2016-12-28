package io.github.iurimenin.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.popularmovies.R;
import io.github.iurimenin.popularmovies.Utils;
import io.github.iurimenin.popularmovies.activity.DetailActivity;
import io.github.iurimenin.popularmovies.activity.SettingsActivity;
import io.github.iurimenin.popularmovies.adapter.MovieAdapter;
import io.github.iurimenin.popularmovies.asynctask.ListMoviesTask;
import io.github.iurimenin.popularmovies.interfaces.AsyncTaskDelegate;
import io.github.iurimenin.popularmovies.valueobject.MovieVO;

/**
 * Created by Iuri on 02/12/16.
 */
public class MovieFragment extends Fragment implements AsyncTaskDelegate {

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
                intent.putExtra(MovieVO.PARCELABLE_KEY, movieVO);
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

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMovies () {

        if (Utils.isNetworkConnected(getContext())) {
            ListMoviesTask fetchWeatherTask = new ListMoviesTask(this);
            fetchWeatherTask.execute(Utils.getSortPreference(getActivity()));
        } else {
            //Se não há	conexão disponível, exibe a mensagem
            View view = getActivity().findViewById(R.id.activity_main);
            Snackbar snackbar = Snackbar.make(view, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
            snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                //Ao clicar na snackbar, uma nova tentativa de atualizar a lista é efetuada :-)
                @Override
                public void onClick(View view) {
                    updateMovies();
                }
            });
            snackbar.show();
        }
    }

    @Override
    public void processFinish(Object output) {

        if(output != null){
            mMovieAdapter.clear();
            mMovieAdapter.addAll((List<MovieVO>) output);
        }else{
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }
}