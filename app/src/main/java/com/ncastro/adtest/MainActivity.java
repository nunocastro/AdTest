package com.ncastro.adtest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.ncastro.adtest.requests.OffersRequest;
import com.ncastro.adtest.utils.FyberUtils;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText apiKeyEditText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AdvertisingIdTask().execute();
        apiKeyEditText = (EditText) findViewById(R.id.info_apikey);
        apiKeyEditText.setText(FyberUtils.API_KEY);
        final View button = findViewById(R.id.button_proceed);
        button.setOnClickListener(onClickListener);

    }

    private class AdvertisingIdTask extends AsyncTask<Void, Integer, AdvertisingIdClient.Info> {
        protected AdvertisingIdClient.Info doInBackground(final Void... params) {
            AdvertisingIdClient.Info adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (final IOException e) {
                // Unrecoverable error connecting to Google Play services (e.g.,
                // the old version of the service doesn't support getting AdvertisingId).
            } catch (final GooglePlayServicesNotAvailableException e) {
                // Google Play services is not available entirely.
            } catch (final GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }

            return adInfo;
        }

        protected void onProgressUpdate(final Integer... progress) {
        }

        protected void onPostExecute(final AdvertisingIdClient.Info advertisingInfo) {
            if (null != advertisingInfo) {
                FyberUtils.GOOGLE_AD_ID = advertisingInfo.getId();
                FyberUtils.GOOGLE_AD_ID_LIMITED = advertisingInfo.isLimitAdTrackingEnabled();
            }
        }
    }

    private boolean areParametersValid() {
        boolean result = true;
        EditText editText = (EditText) findViewById(R.id.info_apikey);
        result = !editText.getText().toString().isEmpty();

        editText = (EditText) findViewById(R.id.info_appid);
        result &= !editText.getText().toString().isEmpty();

        editText = (EditText) findViewById(R.id.info_uid);
        result &= !editText.getText().toString().isEmpty();

        return result;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (areParametersValid()) {
                EditText editText = (EditText) findViewById(R.id.info_apikey);
                FyberUtils.API_KEY = editText.getText().toString();

                final Bundle params = new Bundle();
                editText = (EditText) findViewById(R.id.info_appid);
                params.putString(OffersRequest.APP_ID_PARAM, editText.getText().toString());
                editText = (EditText) findViewById(R.id.info_uid);
                params.putString(OffersRequest.USER_ID_PARAM, editText.getText().toString());
                editText = (EditText) findViewById(R.id.info_pub0);
                params.putString(OffersRequest.CUSTOM_PARAMS_PARAM, editText.getText().toString());

                final Intent intent = new Intent(getApplicationContext(), OffersListActivity.class);
                intent.putExtra(OffersListActivity.PARAMS, params);
                startActivity(intent);
            }

        }
    };

}
