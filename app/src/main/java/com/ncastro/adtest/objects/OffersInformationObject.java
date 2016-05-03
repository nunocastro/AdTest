package com.ncastro.adtest.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ncastro on 4/27/16.
 */
public class OffersInformationObject {

    @SerializedName("app_name")
    private String appName;
    @Expose
    private int appid;
    @SerializedName("virtual_currency")
    private String virtualCurrency;
    @Expose
    private String country;
    @Expose
    private String language;
    @SerializedName("support_url")
    private String supportUrl;

    public String getAppName() {
        return appName;
    }

    public int getAppid() {
        return appid;
    }

    public String getVirtualCurrency() {
        return virtualCurrency;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }

    public String getSupportUrl() {
        return supportUrl;
    }

    public String toHashString() {
        return "appid=" + appid + "&" +
                "app_name=" + appName + "&" +
                "country=" + country + "&" +
                "language=" + language + "&" +
                "support_url=" + supportUrl + "&" +
                "virtual_currency=" + virtualCurrency + "&";
    }
}
