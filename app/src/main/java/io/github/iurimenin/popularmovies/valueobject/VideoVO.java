package io.github.iurimenin.popularmovies.valueobject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iuri Menin on 26/12/16.
 */

public class VideoVO implements Parcelable {

    public static final String PARCELABLE_KEY = "video";

    private String id;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

     /*
     * Parcelable particular
     */

    public static final Parcelable.Creator<VideoVO> CREATOR
            = new Parcelable.Creator<VideoVO>() {
        public VideoVO createFromParcel(Parcel in) {
            return new VideoVO(in);
        }

        public VideoVO[] newArray(int size) {
            return new VideoVO[size];
        }
    };

    private VideoVO(Parcel in) {
        in.readString();
        id = in.readString();
        key = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
