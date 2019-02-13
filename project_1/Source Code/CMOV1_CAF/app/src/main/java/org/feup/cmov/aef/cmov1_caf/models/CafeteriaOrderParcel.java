package org.feup.cmov.aef.cmov1_caf.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CafeteriaOrderParcel implements Parcelable
{
    public String userId;
    public String[] voucherIds;
    public int[] productIds;
    public String signature;

    public CafeteriaOrderParcel()
    {

    }

    public CafeteriaOrderParcel(Parcel in)
    {
        userId = in.readString();
        voucherIds = in.createStringArray();
        productIds = in.createIntArray();
        signature = in.readString();
    }

    public static final Creator<CafeteriaOrderParcel> CREATOR = new Creator<CafeteriaOrderParcel>()
    {
        @Override
        public CafeteriaOrderParcel createFromParcel(Parcel in)
        {
            return new CafeteriaOrderParcel(in);
        }

        @Override
        public CafeteriaOrderParcel[] newArray(int size) {
            return new CafeteriaOrderParcel[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(userId);
        dest.writeStringArray(voucherIds);
        dest.writeIntArray(productIds);
        dest.writeString(signature);
    }
}
