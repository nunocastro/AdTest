package com.ncastro.adtest.requests;

import android.content.Context;

import com.ncastro.adtest.objects.OffersObject;
import com.ncastro.adtest.utils.FyberUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by ncastro on 4/27/16.
 */
public class OffersRequest {

    public final static String APP_ID_PARAM = "appid";
    public final static String USER_ID_PARAM = "uid";
    public final static String CUSTOM_PARAMS_PARAM = "pub0";
    private final static String LOCALE_PARAM = "locale";
    private final static String GOOGLE_AD_ID_PARAM = "google_ad_id";
    private final static String GOOGLE_AD_ID_LIMITED_PARAM = "google_ad_id_limited_tracking_enabled";
    private final static String PAGE_PARAM = "page";
    private final static String OS_VERSION_PARAM = "os_version";
    private final static String TIMESTAMP_PARAM = "timestamp";
    private final static String IP_PARAM = "ip";
    private final static String OFFER_TYPES_PARAM = "offer_types";
    private final static String HASHKEY_PARAM = "hashkey";

    private int page = 1;
    private Locale currentLocale;
    private String applicationId;
    private String userId;
    private int offerType;
    private String customParams;
    private String responseBodyString;

    private Callback<OffersObject> responseCallback;
    private OffersObject responseObject;

    public interface RequestOffers {
        @GET("/feed/v1/offers.json")
        Call<OffersObject> getOffers(@QueryMap Map<String, String> query);
    }

    public OffersRequest setApplicationId(final String appId) {
        this.applicationId = appId;
        return this;
    }

    public OffersRequest setUserId(final String uid) {
        this.userId = uid;
        return this;
    }

    public OffersRequest setOfferType(final int offrType) {
        this.offerType = offrType;
        return this;
    }

    public OffersRequest setCustomParams(final String custParams) {
        this.customParams = custParams;
        return this;
    }

    public OffersRequest setPage(final int currPage) {
        this.page = currPage;
        return this;
    }

    public OffersRequest setResponseCallback(final Callback<OffersObject> callback) {
        this.responseCallback = callback;
        return this;
    }

    public OffersObject getResponse() {
        return responseObject;
    }

    public String getResponseBody() {
        return responseBodyString;
    }

    public OffersRequest execute(final Context context) {
        if (null == currentLocale) {
            currentLocale = context.getResources().getConfiguration().locale;
        }

        return execute();
    }

    private OffersRequest execute() {
        final RequestOffers requestOffers = RESTGenerator.createRequest(RequestOffers.class, new ResponseInterceptor());
        final Call<OffersObject> call = requestOffers.getOffers(getQueryParameters());

        if (null != responseCallback) {
            call.enqueue(responseCallback);
        } else {
            try {
                responseObject = call.execute().body();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    private HashMap<String, String> getQueryParameters() {
        final HashMap<String, String> queryParameters = new HashMap<>();
        final String ipAddress = FyberUtils.getIPv4Address();

        queryParameters.put(OS_VERSION_PARAM, android.os.Build.VERSION.RELEASE);
        queryParameters.put(LOCALE_PARAM, currentLocale.getCountry());
        queryParameters.put(GOOGLE_AD_ID_PARAM, FyberUtils.GOOGLE_AD_ID);
        queryParameters.put(GOOGLE_AD_ID_LIMITED_PARAM, String.valueOf(FyberUtils.GOOGLE_AD_ID_LIMITED));
        if (!ipAddress.isEmpty()) {
            queryParameters.put(IP_PARAM, ipAddress);
        }
        queryParameters.put(OFFER_TYPES_PARAM, String.valueOf(offerType));
        queryParameters.put(APP_ID_PARAM, applicationId);
        queryParameters.put(USER_ID_PARAM, userId);

        if (!customParams.isEmpty()) {
            queryParameters.put(CUSTOM_PARAMS_PARAM, customParams);
        }

        if (page > 1) {
            queryParameters.put(PAGE_PARAM, String.valueOf(page));
        }

        final Long tsLong = System.currentTimeMillis() / 1000;
        queryParameters.put(TIMESTAMP_PARAM, tsLong.toString());
        queryParameters.put(HASHKEY_PARAM, FyberUtils.calculateRequestHash(queryParameters));

        return queryParameters;
    }

    class ResponseInterceptor implements Interceptor {
        @Override
        public Response intercept(final Interceptor.Chain chain) throws IOException {
            final Response response = chain.proceed(chain.request());
            final MediaType contentType = response.body().contentType();

            responseBodyString = response.body().string();
            final ResponseBody body = ResponseBody.create(contentType, responseBodyString);

            return response.newBuilder().body(body).build();
        }
    }


}
