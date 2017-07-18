package com.ik.movienow.common.models;

import android.arch.lifecycle.LiveData;
import android.os.Parcel;
import android.os.Parcelable;

public class Review  implements Parcelable {
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            Review var = new Review();
            var.author = source.readString();
            var.id = source.readString();
            var.content = source.readString();
            var.url = source.readString();
            return var;
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    private String author;
    private String id;
    private String content;
    private String url;

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.id);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
