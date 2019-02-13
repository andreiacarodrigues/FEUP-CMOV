package org.feup.cmov.aef.cmov1_app.files;

import org.feup.cmov.aef.cmov1_app.entitites.Ticket;
import org.feup.cmov.aef.cmov1_app.entitites.Voucher;

import java.util.ArrayList;

public class LoadSaveData
{
    public String UserId = "";
    public String Username;
    public String Password;
    public String NIF;
    public String CreditCardType;
    public String CreditCardNumber;
    public String CreditCardExpiration;
    public ArrayList<Ticket> Tickets = new ArrayList<>();
    public ArrayList<Voucher> Vouchers = new ArrayList<>();
}