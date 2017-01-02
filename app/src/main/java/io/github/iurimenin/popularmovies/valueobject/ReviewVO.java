package io.github.iurimenin.popularmovies.valueobject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iuri Menin on 28/12/16.
 */

public class ReviewVO implements Parcelable {

    public static final String PARCELABLE_KEY = "review";

    private String id;
    private String author;
    private String content;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /*
     * Parcelable particular
     */

    public static final Parcelable.Creator<ReviewVO> CREATOR
            = new Parcelable.Creator<ReviewVO>() {
        public ReviewVO createFromParcel(Parcel in) {
            return new ReviewVO(in);
        }

        public ReviewVO[] newArray(int size) {
            return new ReviewVO[size];
        }
    };

    private ReviewVO(Parcel in) {
        in.readString();
        id = in.readString();
        author = in.readString();
        content  = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
