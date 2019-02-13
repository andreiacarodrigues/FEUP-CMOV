package org.feup.cmov.aef.cmov1_app.entitites;

import java.util.ArrayList;

public class OrderTransaction
{
    public final int Id;
    public final ArrayList<Product> Products;
    public final String Money;
    public final ArrayList<String> Vouchers;


    public OrderTransaction(int Id, ArrayList<Product> products, ArrayList<String> vouchers, String money)
    {
        this.Id = Id;
        this.Products = products;
        this.Money = money;
        this.Vouchers = vouchers;
    }

    public int getId() {
        return Id;
    }

    public ArrayList<Product> getProducts() {
        return Products;
    }

    public String getMoney() {
        return Money;
    }
}
