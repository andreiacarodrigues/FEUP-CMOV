package org.feup.cmov.aef.cmov1_app;

import android.app.Application;
import android.content.Context;

import org.feup.cmov.aef.cmov1_app.entitites.Product;
import org.feup.cmov.aef.cmov1_app.entitites.Ticket;
import org.feup.cmov.aef.cmov1_app.entitites.Voucher;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveData;
import org.feup.cmov.aef.cmov1_app.files.LoadSaveManager;

import java.util.ArrayList;

public class MyApplication extends Application
{
    private static LoadSaveManager loadSaveManager;

    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Voucher> vouchers = new ArrayList<>();
    ArrayList<Ticket> tickets = new ArrayList<>();

    public void onCreate()
    {
        super.onCreate();
        products.add(new Product(1, "Popcorn", "3.0", 0));
        products.add(new Product(2, "Coffee", "0.25", 0));
        products.add(new Product(3, "Soda", "2.0", 0));
        products.add(new Product(4, "Nachos", "1.0", 0));
        products.add(new Product(5, "Water", "1.0", 0));
        products.add(new Product(6, "Candy", "1.0", 0));

        MyApplication.loadSaveManager = new LoadSaveManager(getApplicationContext());
    }

    public static LoadSaveManager getLoadSaveManager()
    {
        return MyApplication.loadSaveManager;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Product> getSelectedProducts() {
        ArrayList<Product> selected = new ArrayList<>();
        for (int x = 0; x < products.size(); x++){
            if(products.get(x).getQuantity() > 0)
                selected.add(products.get(x));
        }
        return selected;
    }

    public ArrayList<Voucher> getSelectedVouchers() {
        return vouchers;
    }

    public ArrayList<Ticket> getSelectedTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setVouchers(ArrayList<Voucher> vouchers) {
        this.vouchers = vouchers;
    }

    public void setProduct(int pos, int quantity) {
        this.products.get(pos).setQuantity(quantity);
    }

    public void clearOrder()
    {
        for(Product product : products)
        {
            product.quantity = 0;
        }
        vouchers.removeAll(getSelectedVouchers());
    }

}