package com.ncastro.adtest.objects;

import com.google.gson.annotations.Expose;

/**
 * Created by ncastro on 4/27/16.
 */
public class ThumbnailsObject {

    @Expose
    private String lowres;
    @Expose
    private String hires;

    public String getLowres() {
        return lowres;
    }

    public String getHires() {
        return hires;
    }

    public String toHashString() {
        return "hires=" + hires + "&" +
                "lowres" + lowres + "&";
    }
}
