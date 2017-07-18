package com.ik.movienow.common.models;

import android.arch.lifecycle.LiveData;
import android.os.Parcel;
import android.os.Parcelable;

public class ReviewList extends LiveData<ReviewList> implements Parcelable {
    public static final Creator<ReviewList> CREATOR = new Creator<ReviewList>() {
        @Override
        public ReviewList createFromParcel(Parcel source) {
            ReviewList var = new ReviewList();
            var.id = source.readInt();
            var.page = source.readInt();
            var.total_pages = source.readInt();
            var.results = source.createTypedArray(Review.CREATOR);
            var.total_results = source.readInt();
            return var;
        }

        @Override
        public ReviewList[] newArray(int size) {
            return new ReviewList[size];
        }
    };
    private int id;
    private int page;
    private int total_pages;
    private Review[] results;
    private int total_results;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return this.total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public Review[] getResults() {
        return this.results;
    }

    public void setResults(Review[] results) {
        this.results = results;
    }

    public int getTotal_results() {
        return this.total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.page);
        dest.writeInt(this.total_pages);
        dest.writeTypedArray(this.results, flags);
        dest.writeInt(this.total_results);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
