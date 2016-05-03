package com.ncastro.adtest.objects;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by ncastro on 4/27/16.
 */
public class OffersObject {

    @Expose
    private String code;
    @Expose
    private String message;
    @Expose
    private int count;
    @Expose
    private int pages;
    @Expose
    private OffersInformationObject information;
    @Expose
    private ArrayList<OfferObject> offers;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getCount() {
        return count;
    }

    public int getPages() {
        return pages;
    }

    public OffersInformationObject getInformation() {
        return information;
    }

    public ArrayList<OfferObject> getOffers() {
        return offers;
    }
}
