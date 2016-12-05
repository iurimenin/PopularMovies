package io.github.iurimenin.popularmovies.valueobject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iuri on 02/12/16.
 */

public class MovieVO implements Parcelable {

    private String original_title;
    private String release_date;
    private String poster_path;
    private String vote_average;
    private String overview;

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

     /*
     * Parcelable particular
     */

    public static final String PARCELABLE_KEY = "movie";

    public static final Parcelable.Creator<MovieVO> CREATOR
            = new Parcelable.Creator<MovieVO>() {
        public MovieVO createFromParcel(Parcel in) {
            return new MovieVO(in);
        }

        public MovieVO[] newArray(int size) {
            return new MovieVO[size];
        }
    };

    private MovieVO(Parcel in) {
        original_title = in.readString();
        poster_path  = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(vote_average);
        dest.writeString(release_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
