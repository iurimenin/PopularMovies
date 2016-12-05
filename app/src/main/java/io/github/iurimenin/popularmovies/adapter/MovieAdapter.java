package io.github.iurimenin.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.popularmovies.valueobject.MovieVO;
import io.github.iurimenin.popularmovies.R;
import io.github.iurimenin.popularmovies.Utils;

/**
 * Created by Iuri Menin on 02/12/16.
 */

public class MovieAdapter extends ArrayAdapter<MovieVO> {

    @BindView(R.id.movie_image) ImageView mMovieImage;

    public MovieAdapter(Context context, List<MovieVO> movieVOs) {
        super(context, 0, movieVOs);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieVO movieVO = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.movie_item, parent, false);
        }

        ButterKnife.bind(this, convertView);

        Picasso.with(getContext()).load(Utils.getImageUrl185(movieVO.getPoster_path())).into(mMovieImage);

        return convertView;
    }
}
