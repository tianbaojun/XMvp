package com.zjhz.teacher.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

/**
 * Created by zzd on 2017/6/13.
 */

public class Dict implements Serializable, Parcelable ,Comparable{
    public String id;
    public String value;

    public static final Parcelable.Creator<Dict> CREATOR = new Parcelable.Creator<Dict>() {
        @Override
        public Dict createFromParcel(Parcel source) {
            return new Dict(source);
        }

        @Override
        public Dict[] newArray(int size) {
            return new Dict[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Dict(Parcel in) {
        this.id = in.readString();
        this.value = in.readString();
    }

    public Dict() {
    }

    public Dict(String id, String value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(@NonNull Object another) {
        Dict r = (Dict) another;
        Comparator<Object> cmp = Collator.getInstance(java.util.Locale.CHINA);
        return cmp.compare(this.value, r.value);
    }
}
