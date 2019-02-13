package org.feup.cmov.aef.cmov1_app.responses;

import org.feup.cmov.aef.cmov1_app.entitites.Order;
import org.feup.cmov.aef.cmov1_app.entitites.Purchase;
import org.feup.cmov.aef.cmov1_app.entitites.Ticket;
import org.feup.cmov.aef.cmov1_app.entitites.Voucher;

public class ConsultTransactionsResponse
{
    public enum ResponseValue
    {
        Success,
        UserNotFound,
        UnknownError
    }

    public Purchase[] purchases;
    public Ticket[] tickets;
    public Voucher[] vouchers;
    public Order[] orders;
    public ResponseValue response = ResponseValue.Success;
}
