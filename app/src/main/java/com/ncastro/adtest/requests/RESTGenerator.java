package com.ncastro.adtest.requests;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ncastro on 4/27/16.
 */
public class RESTGenerator {
    public static final String FYBER_BASE_URL = "http://api.fyber.com/";

    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private static final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

    private static Retrofit retrofit;

    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(FYBER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <T> T createRequest(final Class<T> serviceClass, Interceptor responseInterceptor) {
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        retrofit = builder.client(
                okHttpClient
                        .addInterceptor(logging)
                        .addInterceptor(responseInterceptor)
                        .build()
        ).build();
        return retrofit.create(serviceClass);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

}
