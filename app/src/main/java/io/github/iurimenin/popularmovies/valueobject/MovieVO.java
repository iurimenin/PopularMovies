package io.github.iurimenin.popularmovies.valueobject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iuri on 02/12/16.
 */

public class MovieVO implements Parcelable {

    public static final String PARCELABLE_KEY = "movie";

    private String id;
    private String original_title;
    private String release_date;
    private String poster_path;
    private String vote_average;
    private String overview;
    private List<VideoVO> videos;
    private List<ReviewVO> reviews;
    private boolean favorite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<VideoVO> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoVO> videos) {
        this.videos = videos;
    }

    public List<ReviewVO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewVO> reviews) {
        this.reviews = reviews;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /*
     * Parcelable particular
     */
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
        id = in.readString();
        original_title = in.readString();
        poster_path  = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
        videos = new ArrayList<>();
        in.readTypedList(videos, VideoVO.CREATOR);
        reviews = new ArrayList<>();
        in.readTypedList(reviews, ReviewVO.CREATOR);
        favorite = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(vote_average);
        dest.writeString(release_date);
        dest.writeList(videos);
        dest.writeList(reviews);
        dest.writeInt(favorite ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
