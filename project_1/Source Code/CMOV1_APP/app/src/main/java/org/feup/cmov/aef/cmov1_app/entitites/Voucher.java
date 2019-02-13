package org.feup.cmov.aef.cmov1_app.entitites;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Voucher implements Parcelable
{
    public String id;
    public String type;
    public boolean selected = false;

    public Voucher(String id, String type)
    {
        this.id = id;
        this.type = type;
    }

    protected Voucher(Parcel in) {
        id = in.readString();
        type = in.readString();
    }

    public static final Creator<Voucher> CREATOR = new Creator<Voucher>()
    {
        @Override
        public Voucher createFromParcel(Parcel in)
        {
            return new Voucher(in);
        }

        @Override
        public Voucher[] newArray(int size)
        {
            return new Voucher[size];
        }
    };

    public String getType() {
        return type;
    }

    @NonNull
    @Override
    public String toString() {
        return type;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(type);
    }
}
