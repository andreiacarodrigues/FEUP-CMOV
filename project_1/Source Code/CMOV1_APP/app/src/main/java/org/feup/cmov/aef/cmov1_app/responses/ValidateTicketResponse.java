package org.feup.cmov.aef.cmov1_app.responses;

import android.os.Parcel;
import android.os.Parcelable;

public class ValidateTicketResponse implements Parcelable
{
    public enum ResponseValue
    {
        Success,
        UserNotFound,
        CanOnlyValidateUpTo4TicketsAtOnce,
        CouldNotFindTicket,
        CouldNotFindPurchase,
        TicketsAreNotAllForTheSamePerformance,
        NotTicketOwner,
        UnknownError
    }

    public String[] validatedTicketIds;
    public ResponseValue response = ResponseValue.Success;

    public ValidateTicketResponse(Parcel in) {
        validatedTicketIds = in.createStringArray();
        response = ResponseValue.values()[in.readInt()];
    }

    public static final Creator<ValidateTicketResponse> CREATOR = new Creator<ValidateTicketResponse>() {
        @Override
        public ValidateTicketResponse createFromParcel(Parcel in) {
            return new ValidateTicketResponse(in);
        }

        @Override
        public ValidateTicketResponse[] newArray(int size) {
            return new ValidateTicketResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(validatedTicketIds);
        dest.writeInt(response.ordinal());
    }
}
