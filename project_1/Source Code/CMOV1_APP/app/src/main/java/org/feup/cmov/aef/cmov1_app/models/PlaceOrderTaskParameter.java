package org.feup.cmov.aef.cmov1_app.models;

public class PlaceOrderTaskParameter
{
    public final int[] voucherIds;
    public final int[] productIds;

    public PlaceOrderTaskParameter(int[] voucherIds, int[] productIds)
    {
        this.voucherIds = voucherIds;
        this.productIds = productIds;
    }
}
