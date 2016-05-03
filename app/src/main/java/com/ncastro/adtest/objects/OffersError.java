package com.ncastro.adtest.objects;

import com.google.gson.annotations.Expose;

/**
 * Created by Nuno on 03/05/2016.
 */
public class OffersError {

    @Expose
    private String code;
    @Expose
    private String message;

    public OffersError() {
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
