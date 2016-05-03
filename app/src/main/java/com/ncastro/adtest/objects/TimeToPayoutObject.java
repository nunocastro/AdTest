package com.ncastro.adtest.objects;

import com.google.gson.annotations.Expose;

/**
 * Created by ncastro on 4/27/16.
 */
public class TimeToPayoutObject {

    @Expose
    private int amount;
    @Expose
    private String readable;

    public int getAmount() {
        return amount;
    }

    public String getReadable() {
        return readable;
    }

    public String toHashString() {
        return "amount=" + String.valueOf(amount) + "&" +
                "readable=" + readable + "&";
    }
}
