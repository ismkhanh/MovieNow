package com.ik.movienow.common.models;

import android.arch.lifecycle.LiveData;
import android.os.Parcel;
import android.os.Parcelable;

public class Trailer  implements Parcelable {
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            Trailer var = new Trailer();
            var.site = source.readString();
            var.size = source.readInt();
            var.iso_3166_1 = source.readString();
            var.name = source.readString();
            var.id = source.readString();
            var.type = source.readString();
            var.iso_639_1 = source.readString();
            var.key = source.readString();
            return var;
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    private String site;
    private int size;
    private String iso_3166_1;
    private String name;
    private String id;
    private String type;
    private String iso_639_1;
    private String key;

    public String getSite() {
        return this.site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getIso_3166_1() {
        return this.iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIso_639_1() {
        return this.iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.iso_3166_1);
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.iso_639_1);
        dest.writeString(this.key);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
