package org.feup.cmov.aef.cmov1_app.entitites;

import java.io.Serializable;

public class User implements Serializable
{
    private String Username;

    private String Password;

    private String Name;

    private String NIF;

    private String CreditCardNumber;

    private String CreditCardType;

    private String CreditCardExpiration;

    private String PublicKeyModulus;

    private String PublicKeyExponent;

    public String getUsername()
    {
        return Username;
    }

    public void setUsername(String username)
    {
        Username = username;
    }

    public String getPassword()
    {
        return Password;
    }

    public void setPassword(String password)
    {
        Password = password;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getNIF()
    {
        return NIF;
    }

    public void setNIF(String NIF)
    {
        this.NIF = NIF;
    }

    public String getCreditCardNumber()
    {
        return CreditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber)
    {
        CreditCardNumber = creditCardNumber;
    }

    public String getCreditCardType()
    {
        return CreditCardType;
    }

    public void setCreditCardType(String creditCardType)
    {
        CreditCardType = creditCardType;
    }

    public String getCreditCardExpiration()
    {
        return CreditCardExpiration;
    }

    public void setCreditCardExpiration(String creditCardExpiration)
    {
        CreditCardExpiration = creditCardExpiration;
    }

    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Username);
        stringBuilder.append('\n');
        stringBuilder.append(Password);
        stringBuilder.append('\n');
        stringBuilder.append(Name);
        stringBuilder.append('\n');
        stringBuilder.append(NIF);
        stringBuilder.append('\n');
        stringBuilder.append(CreditCardNumber);
        stringBuilder.append('\n');
        stringBuilder.append(CreditCardType);
        stringBuilder.append('\n');
        stringBuilder.append(CreditCardExpiration);
        stringBuilder.append('\n');
        stringBuilder.append(getPublicKeyModulus());
        stringBuilder.append('\n');
        stringBuilder.append(getPublicKeyExponent());
        return stringBuilder.toString();
    }

    public String getPublicKeyModulus() {
        return PublicKeyModulus;
    }

    public void setPublicKeyModulus(String publicKeyModulus) {
        PublicKeyModulus = publicKeyModulus;
    }

    public String getPublicKeyExponent() {
        return PublicKeyExponent;
    }

    public void setPublicKeyExponent(String publicKeyExponent) {
        PublicKeyExponent = publicKeyExponent;
    }
}