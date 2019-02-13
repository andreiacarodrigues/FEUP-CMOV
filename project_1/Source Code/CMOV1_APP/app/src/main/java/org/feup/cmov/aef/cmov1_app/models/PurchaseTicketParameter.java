package org.feup.cmov.aef.cmov1_app.models;

public class PurchaseTicketParameter
{
    public final int PerformanceId;
    public final int NumberOfTickets;

    public PurchaseTicketParameter(int performanceId, int numberOfTickets)
    {
        PerformanceId = performanceId;
        NumberOfTickets = numberOfTickets;
    }
}
