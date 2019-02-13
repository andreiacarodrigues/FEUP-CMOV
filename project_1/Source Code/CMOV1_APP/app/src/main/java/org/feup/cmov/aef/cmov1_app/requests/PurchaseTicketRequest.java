package org.feup.cmov.aef.cmov1_app.requests;

public class PurchaseTicketRequest
{
    public final int PerformanceId;
    public final int NumberOfTickets;
    public final String UserId;
    public final String Signature;

    public PurchaseTicketRequest(int performanceId, int numberOfTickets, String userId, String signature)
    {
        PerformanceId = performanceId;
        NumberOfTickets = numberOfTickets;
        UserId = userId;
        Signature = signature;
    }
}
