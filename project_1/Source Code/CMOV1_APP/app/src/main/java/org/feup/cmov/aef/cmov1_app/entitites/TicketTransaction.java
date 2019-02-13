package org.feup.cmov.aef.cmov1_app.entitites;

import androidx.annotation.NonNull;

public class TicketTransaction
{
    public final int Id;
    public final Performance Performance;
    public int Quantity;

    public TicketTransaction(int Id,  Performance performance, int quantity)
    {
        this.Id = Id;
        this.Performance = performance;
        this.Quantity = quantity;
    }

    public int getId() {
        return Id;
    }

    public Performance getPerformance() {
        return Performance;
    }

    public int getQuantity() {
        return Quantity;
    }

}
