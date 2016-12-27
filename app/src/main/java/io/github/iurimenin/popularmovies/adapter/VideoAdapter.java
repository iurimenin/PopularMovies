package io.github.iurimenin.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.iurimenin.popularmovies.R;
import io.github.iurimenin.popularmovies.valueobject.VideoVO;

/**
 * Created by Iuri Menin on 26/12/16.
 */

public class VideoAdapter extends ArrayAdapter<VideoVO> {

    @BindView(R.id.video_text) TextView mVideoText;
    @BindView(R.id.video_image) ImageView mVideoImage;

    public VideoAdapter(Context context, List<VideoVO> movieVOs) {
        super(context, 0, movieVOs);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.video_item, parent, false);
        }

        ButterKnife.bind(this, convertView);

        mVideoText.setText(getContext().getString(R.string.trailer, String.valueOf(position + 1)));

        return convertView;
    }
}
