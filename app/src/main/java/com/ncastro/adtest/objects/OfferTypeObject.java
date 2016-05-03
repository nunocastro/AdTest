package com.ncastro.adtest.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ncastro on 4/27/16.
 */
public class OfferTypeObject {

    @SerializedName("offer_type_id")
    private int offerTypeId;
    @Expose
    private String readable;

    public int getOfferTypeId() {
        return offerTypeId;
    }

    public String getReadable() {
        return readable;
    }

    public String toHashString() {
        return "offerTypeId=" + String.valueOf(offerTypeId) + "&" +
                "readable=" + readable + "&";
    }
}
