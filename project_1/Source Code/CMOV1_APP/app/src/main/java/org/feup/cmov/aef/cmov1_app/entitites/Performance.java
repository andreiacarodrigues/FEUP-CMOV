package org.feup.cmov.aef.cmov1_app.entitites;

import androidx.annotation.NonNull;

public class Performance
{
    public int id;
    public String name;
    public String date;
    public String price;

    public Performance(int Id, String name, String date, String price)
    {
        this.id = Id;
        this.name = name;
        this.date = date;
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
