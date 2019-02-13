package org.feup.cmov.aef.cmov1_caf.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Product implements Parcelable
{
    public int id;
    public String name;
    public String price;
    public int quantity;

    public Product(int Id, String name, String price, int quantity)
    {
        this.id = Id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    protected Product(Parcel in)
    {
        id = in.readInt();
        name = in.readString();
        price = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>()
    {
        @Override
        public Product createFromParcel(Parcel in)
        {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size)
        {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeInt(quantity);
    }
}
