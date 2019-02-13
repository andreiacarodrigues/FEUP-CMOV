package org.feup.cmov.aef.cmov1_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.feup.cmov.aef.cmov1_app.entitites.Product;
import org.feup.cmov.aef.cmov1_app.entitites.Voucher;

import java.util.ArrayList;

public class CafeteriaOrderParcel implements Parcelable
{
    public String userId;
    public String[] voucherIds;
    public int[] productIds;
    public String signature;

    public CafeteriaOrderParcel()
    {

    }

    protected CafeteriaOrderParcel(Parcel in)
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

    public void setVoucherIds(Voucher[] voucherIds)
    {
        ArrayList<String> array = new ArrayList<>();
        for(Voucher voucher : voucherIds)
        {
            array.add(voucher.id);
        }
        this.voucherIds = array.toArray(new String[0]);
    }

    public void setProductIds(Product[] productIds)
    {
        ArrayList<Integer> array = new ArrayList<>();
        for(Product product : productIds)
        {
            for(int i = 0; i < product.quantity; i++)
            {
                array.add(product.id);
            }
        }
        int[] intArray = new int[array.size()];
        for(int i = 0; i < array.size(); i++)
        {
            intArray[i] = array.get(i);
        }
        this.productIds = intArray;
    }
}
