package org.feup.cmov.aef.cmov1_app.responses;

import org.feup.cmov.aef.cmov1_app.entitites.Ticket;
import org.feup.cmov.aef.cmov1_app.entitites.Voucher;

public class PurchaseTicketResponse
{
    public Ticket[] tickets;
    public Voucher[] vouchers;
}
