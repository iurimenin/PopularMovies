package io.github.iurimenin.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.popularmovies.R;
import io.github.iurimenin.popularmovies.valueobject.ReviewVO;

/**
 * Created by Iuri Menin on 30/12/16.
 */

public class ReviewAdapter extends ArrayAdapter<ReviewVO> {

    @BindView(R.id.review_author) protected TextView mReviewAuthor;
    @BindView(R.id.review_content) protected TextView mReviewContent;

    public ReviewAdapter(Context context,  List<ReviewVO> reviews) {
        super(context, 0, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.review_item, parent, false);
        }

        ButterKnife.bind(this, convertView);

        mReviewAuthor.setText(getItem(position).getAuthor());
        mReviewContent.setText(getItem(position).getContent());

        return convertView;
    }
}