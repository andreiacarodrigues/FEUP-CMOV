package org.feup.cmov.aef.cmov1_app.responses;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaceOrderTaskResponse implements Parcelable
{
    public enum ResponseValue
    {
        Success,
        InvalidSignature,
        MustChooseProducts,
        CanOnlyUsedAtMost2Vouchers,
        UserDoesNotOwnVoucher,
        AlreadyUsedVoucherSent,
        OnlyOne5DiscountCanBeUsedPerOrder,
        UserNotFound,
        VoucherNotFound,
        ProductNotFound,
        UnknownError
    }

    public int orderId;
    public int[] acceptedVouchers;
    public float totalPrice;
    public ResponseValue response = ResponseValue.Success;

    public PlaceOrderTaskResponse()
    {

    }

    public PlaceOrderTaskResponse(Parcel in)
    {
        orderId = in.readInt();
        acceptedVouchers = in.createIntArray();
        totalPrice = in.readFloat();
        response = ResponseValue.values()[in.readInt()];
    }

    public static final Creator<PlaceOrderTaskResponse> CREATOR = new Creator<PlaceOrderTaskResponse>()
    {
        @Override
        public PlaceOrderTaskResponse createFromParcel(Parcel in) {
            return new PlaceOrderTaskResponse(in);
        }

        @Override
        public PlaceOrderTaskResponse[] newArray(int size) {
            return new PlaceOrderTaskResponse[size];
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
        dest.writeInt(orderId);
        dest.writeIntArray(acceptedVouchers);
        dest.writeFloat(totalPrice);
        dest.writeInt(response.ordinal());
    }
}
