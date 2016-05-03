package com.ncastro.adtest.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ncastro on 4/27/16.
 */
public class OfferObject {

    @Expose
    private String link;
    @Expose
    private String title;
    @SerializedName("offer_id")
    private int offerId;
    @Expose
    private String teaser;
    @SerializedName("required_actions")
    private String requiredActions;
    @Expose
    private ThumbnailsObject thumbnail;
    @SerializedName("offer_types")
    private ArrayList<OfferTypeObject> offerTypes;
    @Expose
    private int payout;
    @SerializedName("time_to_payout")
    private TimeToPayoutObject timeToPayout;

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public int getOfferId() {
        return offerId;
    }

    public String getTeaser() {
        return teaser;
    }

    public String getRequiredActions() {
        return requiredActions;
    }

    public ThumbnailsObject getThumbnail() {
        return thumbnail;
    }

    public ArrayList<OfferTypeObject> getOfferTypes() {
        return offerTypes;
    }

    public int getPayout() {
        return payout;
    }

    public TimeToPayoutObject getTimeToPayout() {
        return timeToPayout;
    }

}
