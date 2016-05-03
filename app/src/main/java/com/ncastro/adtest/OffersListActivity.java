package com.ncastro.adtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ncastro.adtest.adapters.OffersListAdapter;
import com.ncastro.adtest.objects.OfferObject;
import com.ncastro.adtest.objects.OffersError;
import com.ncastro.adtest.objects.OffersObject;
import com.ncastro.adtest.requests.OffersRequest;
import com.ncastro.adtest.utils.FyberUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersListActivity extends AppCompatActivity {

    public final static String PARAMS = "params";
    private final static String HEADER_HASH_KEY = "X-Sponsorpay-Response-Signature";
    private final static int OFFER_TYPE = 112;
    private static final int GRID_NUM_COLUMNS = 1;
    private static final int BOTTOM_ITEM_LIMIT = 4;
    private static final int NO_MORE_PAGES = -1;

    private ArrayList<OfferObject> offerObjectArrayList;
    private RecyclerView offersRecyclerView;
    private SwipeRefreshLayout refreshContainer;
    private FloatingActionButton backToTopButton;
    private View errorContainer;
    private TextView errorMessage;

    private boolean isExecutingRequest = false;
    private boolean isRefreshing = false;
    private int nextPage = 1;

    private OffersRequest offersRequest;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_list);

        final Intent intent = getIntent();
        final Bundle params = intent.getBundleExtra(PARAMS);

        offerObjectArrayList = new ArrayList<>();

        offersRequest = new OffersRequest()
                .setResponseCallback(offersCallback)
                .setApplicationId(params.getString(OffersRequest.APP_ID_PARAM))
                .setUserId(params.getString(OffersRequest.USER_ID_PARAM))
                .setOfferType(OFFER_TYPE)
                .setCustomParams(params.getString(OffersRequest.CUSTOM_PARAMS_PARAM))
                .execute(getApplicationContext());

        refreshContainer = (SwipeRefreshLayout) findViewById(R.id.refresh_container);
        refreshContainer.setOnRefreshListener(onRefreshListener);

        final GridLayoutManager gridManager = new GridLayoutManager(this, GRID_NUM_COLUMNS);

        offersRecyclerView = (RecyclerView) findViewById(R.id.offers_list);
        offersRecyclerView.setHasFixedSize(true);
        offersRecyclerView.setLayoutManager(gridManager);
        offersRecyclerView.addOnScrollListener(onScrollListener);

        errorContainer = findViewById(R.id.error_container);
        errorMessage = (TextView) findViewById(R.id.error_message);

        backToTopButton = (FloatingActionButton) findViewById(R.id.back_top_fab);
        backToTopButton.setOnClickListener(onClickListener);

    }

    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() > 2) {
                backToTopButton.setVisibility(View.VISIBLE);
            } else {
                backToTopButton.setVisibility(View.GONE);
            }

            if (((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() > recyclerView.getLayoutManager().getItemCount() - BOTTOM_ITEM_LIMIT
                    && NO_MORE_PAGES != nextPage) {
                final OffersListAdapter offersListAdapter = (OffersListAdapter) offersRecyclerView.getAdapter();
                offersListAdapter.setShowLoading(true);
                offersListAdapter.notifyDataSetChanged();

                requestOffersListPage(nextPage);
            }
        }
    };

    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            nextPage = 1;
            isRefreshing = true;
            requestOffersListPage(nextPage);
        }
    };

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            offersRecyclerView.scrollToPosition(4);
            offersRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    offersRecyclerView.smoothScrollToPosition(0);
                }
            });
        }
    };

    private final Callback<OffersObject> offersCallback = new Callback<OffersObject>() {
        @Override
        public void onResponse(final Call<OffersObject> call, final Response<OffersObject> response) {


            if (response.isSuccessful()) {
                if (response.headers().get(HEADER_HASH_KEY).equals(FyberUtils.calculateResponseHash(offersRequest.getResponseBody()))) {
                    final OffersObject offersObject = response.body();

                    if (offersObject.getCount() == 0 && nextPage == 1) {
                        showErrorInformation(getString(R.string.no_offers_text));
                    } else {
                        if (nextPage == 1) {
                            hideErrorInformation();
                            offerObjectArrayList.clear();
                        }
                        offerObjectArrayList.addAll(offersObject.getOffers());

                        OffersListAdapter offersListAdapter = (OffersListAdapter) offersRecyclerView.getAdapter();
                        if (null == offersListAdapter) {
                            offersListAdapter = new OffersListAdapter(offersObject.getOffers());
                            offersRecyclerView.setAdapter(offersListAdapter);
                        } else {
                            offersListAdapter.setShowLoading(false);
                            offersListAdapter.notifyDataSetChanged();
                        }

                        if (nextPage != offersObject.getPages()) {
                            nextPage++;
                        } else {
                            nextPage = NO_MORE_PAGES;
                        }
                        isExecutingRequest = false;
                        if (isRefreshing) {
                            refreshContainer.setRefreshing(false);
                            isRefreshing = false;
                        }
                    }
                } else {
                    showErrorInformation(getString(R.string.reponse_hash_error));
                }
            } else {
                final OffersError offersError = FyberUtils.parseError(response);
                showErrorInformation(offersError.getMessage());
            }
        }

        @Override
        public void onFailure(final Call<OffersObject> call, final Throwable t) {
            Log.d("_onResponse", "onFailure: " + offersRequest.getResponseBody());

        }
    };

    private void hideErrorInformation() {
        offersRecyclerView.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
    }

    private void showErrorInformation(final String message) {
        errorMessage.setText(message);

        offersRecyclerView.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
    }

    private void requestOffersListPage(final int page) {
        if (NO_MORE_PAGES != page && !isExecutingRequest) {
            isExecutingRequest = true;

            offersRequest.setPage(page)
                    .execute(getApplicationContext());
        }
    }
}
