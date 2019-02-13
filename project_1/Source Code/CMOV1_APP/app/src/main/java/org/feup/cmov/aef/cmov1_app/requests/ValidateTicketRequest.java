package org.feup.cmov.aef.cmov1_app.requests;

import android.os.Parcel;
import android.os.Parcelable;

public class ValidateTicketRequest implements Parcelable
{
    public String userId;
    public int numberOfTickets;
    public String[] ticketIds;
    public int[] ticketSeatNumbers;
    public String performanceName;
    public String performanceDate;

    public ValidateTicketRequest(String userId, int numberOfTickets, String[] ticketIds, int[] ticketSeatNumbers, String performanceName, String performanceDate)
    {
        this.userId = userId;
        this.numberOfTickets = numberOfTickets;
        this.ticketIds = ticketIds;
        this.ticketSeatNumbers = ticketSeatNumbers;
        this.performanceName = performanceName;
        this.performanceDate = performanceDate;
    }

    public ValidateTicketRequest(Parcel in) {
        userId = in.readString();
        numberOfTickets = in.readInt();
        ticketIds = in.createStringArray();
        ticketSeatNumbers = in.createIntArray();
        performanceName = in.readString();
        performanceDate = in.readString();
    }

    public static final Creator<ValidateTicketRequest> CREATOR = new Creator<ValidateTicketRequest>() {
        @Override
        public ValidateTicketRequest createFromParcel(Parcel in) {
            return new ValidateTicketRequest(in);
        }

        @Override
        public ValidateTicketRequest[] newArray(int size) {
            return new ValidateTicketRequest[size];
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
        dest.writeInt(numberOfTickets);
        dest.writeStringArray(ticketIds);
        dest.writeIntArray(ticketSeatNumbers);
        dest.writeString(performanceName);
        dest.writeString(performanceDate);
    }
}
