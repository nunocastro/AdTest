package com.ncastro.adtest.utils;

import com.ncastro.adtest.objects.OffersError;
import com.ncastro.adtest.requests.RESTGenerator;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by ncastro on 4/27/16.
 */
public class FyberUtils {

    public static String GOOGLE_AD_ID;
    public static boolean GOOGLE_AD_ID_LIMITED;
    public static String API_KEY = "1c915e3b5d42d05136185030892fbb846c278927";

    public static String calculateRequestHash(final HashMap<String, String> parameters) {
        String hashBase = "";
        final Map<String, String> map = new TreeMap<>(parameters);
        for (final String key : map.keySet()) {
            hashBase += key + "=" + map.get(key) + "&";
        }
        hashBase += API_KEY;
        return new String(Hex.encodeHex(DigestUtils.sha(hashBase)));
    }

    public static String calculateResponseHash(final String response) {
        String hashBase = response + API_KEY;
        return new String(Hex.encodeHex(DigestUtils.sha(hashBase)));
    }

    public static String getIPv4Address() {
        String ipAddress = "";
        try {
            final List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            final int size = networkInterfaces.size();
            NetworkInterface networkInterface;
            String addressString;
            for (int x = 0; x < size && ipAddress.isEmpty(); x++) {
                networkInterface = networkInterfaces.get(x);
                final List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                for (final InetAddress address : addresses) {
                    if (!address.isLoopbackAddress()) {
                        addressString = address.getHostAddress();
                        if (addressString.indexOf(':') < 0) {
                            ipAddress = addressString;
                            break;
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        return ipAddress;
    }

    public static OffersError parseError(final Response<?> response) {
        final Converter<ResponseBody, OffersError> converter =
                RESTGenerator.getRetrofit().responseBodyConverter(OffersError.class, new Annotation[0]);

        final OffersError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (final IOException e) {
            return new OffersError();
        }

        return error;
    }


}
