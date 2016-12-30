package io.github.iurimenin.popularmovies.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.popularmovies.R;
import io.github.iurimenin.popularmovies.Utils;
import io.github.iurimenin.popularmovies.activity.SettingsActivity;
import io.github.iurimenin.popularmovies.adapter.ReviewAdapter;
import io.github.iurimenin.popularmovies.adapter.VideoAdapter;
import io.github.iurimenin.popularmovies.valueobject.MovieVO;
import io.github.iurimenin.popularmovies.valueobject.VideoVO;

/**
 * Created by Iuri Menin on 02/12/16.
 */
public class MovieDetailFragment extends Fragment {

    @BindView(R.id.text_view_tittle) TextView mTextViewTittle;
    @BindView(R.id.list_view_reviews) ListView mListViewReviews;
    @BindView(R.id.text_view_synopsis) TextView mTextViewSynopsis;
    @BindView(R.id.list_view_trailers) ListView mListViewTrailers;
    @BindView(R.id.image_view_movie_poster) ImageView mMoviePoster;
    @BindView(R.id.text_view_release_date) TextView mTextViewReleaseDate;
    @BindView(R.id.text_view_vote_avarage) TextView mTextViewVoteAvarage;

    private MovieVO movieVO;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        movieVO = getActivity().getIntent().getExtras().getParcelable(MovieVO.PARCELABLE_KEY);

        mTextViewTittle.setText(movieVO.getOriginal_title());
        Picasso.with(getContext()).load(Utils.getImageUrl780(movieVO.getPoster_path())).into(mMoviePoster);
        mTextViewReleaseDate.setText(Utils.convertDate(this.getContext(), movieVO.getRelease_date()));
        mTextViewVoteAvarage.setText(movieVO.getVote_average());
        mTextViewSynopsis.setText(movieVO.getOverview());
        mVideoAdapter = new VideoAdapter(getActivity(), movieVO.getVideos());
        mListViewTrailers.setAdapter(mVideoAdapter);
        mListViewTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VideoVO videoVO = mVideoAdapter.getItem(i);
                watchYoutubeVideo(videoVO.getKey());
            }
        });

        mReviewAdapter = new ReviewAdapter(getActivity(), movieVO.getReviews());
        mListViewReviews.setAdapter(mReviewAdapter);

        setListViewHeightBasedOnChildren(mListViewTrailers);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_favorite) {
            handleFavoriteClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleFavoriteClick() {

        boolean isFavorite = Utils.isFavoriteMovie(getContext(), movieVO.getId());
        if (isFavorite)
            Toast.makeText(getContext(), getString(R.string.removed_favorite), Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getContext(), getString(R.string.favorited), Toast.LENGTH_LONG).show();

        Utils.markAsFavorite(getContext(), movieVO.getId(), !isFavorite);
    }

    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
