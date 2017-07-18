package com.ik.movienow.common.models;

import android.arch.lifecycle.LiveData;
import android.os.Parcel;
import android.os.Parcelable;

public class TrailerList extends LiveData<TrailerList>  implements Parcelable {
    public static final Creator<TrailerList> CREATOR = new Creator<TrailerList>() {
        @Override
        public TrailerList createFromParcel(Parcel source) {
            TrailerList var = new TrailerList();
            var.id = source.readInt();
            var.results = source.createTypedArray(Trailer.CREATOR);
            return var;
        }

        @Override
        public TrailerList[] newArray(int size) {
            return new TrailerList[size];
        }
    };
    private int id;
    private Trailer[] results;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Trailer[] getResults() {
        return this.results;
    }

    public void setResults(Trailer[] results) {
        this.results = results;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedArray(this.results, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
