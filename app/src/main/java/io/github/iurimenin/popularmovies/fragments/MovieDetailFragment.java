package io.github.iurimenin.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.popularmovies.R;
import io.github.iurimenin.popularmovies.Utils;
import io.github.iurimenin.popularmovies.adapter.VideoAdapter;
import io.github.iurimenin.popularmovies.valueobject.MovieVO;
import io.github.iurimenin.popularmovies.valueobject.VideoVO;

/**
 * Created by Iuri Menin on 02/12/16.
 */
public class MovieDetailFragment extends Fragment {

    @BindView(R.id.text_view_tittle) TextView mTextViewTittle;
    @BindView(R.id.text_view_synopsis) TextView mTextViewSynopsis;
    @BindView(R.id.list_view_trailers) ListView mListViewTrailers;
    @BindView(R.id.image_view_movie_poster) ImageView mMoviePoster;
    @BindView(R.id.text_view_release_date) TextView mTextViewReleaseDate;
    @BindView(R.id.text_view_vote_avarage) TextView mTextViewVoteAvarage;

    private VideoAdapter mVideoAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        MovieVO movieVO = getActivity().getIntent().getExtras().getParcelable(MovieVO.PARCELABLE_KEY);

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
//                Intent intent = new Intent(MovieFragment.this.getActivity(), DetailActivity.class);
//                intent.putExtra(MovieVO.PARCELABLE_KEY, movieVO);
//                startActivity(intent);
            }
        });

        return rootView;
    }
}
