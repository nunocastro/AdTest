package com.ncastro.adtest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ncastro.adtest.R;
import com.ncastro.adtest.objects.OfferObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ncastro on 5/2/16.
 */
public class OffersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEWTYPE_ITEM = 1;
    public static final int VIEWTYPE_LOADER = 2;

    private ArrayList<OfferObject> dataset;
    private boolean showFooter = false;

    private static class OfferObjectHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView teaser;
        TextView payout;

        public OfferObjectHolder(final View itmView) {
            super(itmView);
            thumbnail = (ImageView) itmView.findViewById(R.id.thumbnail);
            title = (TextView) itmView.findViewById(R.id.title);
            teaser = (TextView) itmView.findViewById(R.id.teaser);
            payout = (TextView) itmView.findViewById(R.id.payout);
        }
    }

    private static class ProgressHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public ProgressHolder(final View itmView) {
            super(itmView);
            progressBar = (ProgressBar) itmView.findViewById(R.id.progressBar);
        }
    }

    public OffersListAdapter(final ArrayList<OfferObject> data) {
        this.dataset = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view;
        final RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case VIEWTYPE_LOADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_loader, parent, false);
                viewHolder = new ProgressHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_card, parent, false);
                viewHolder = new OfferObjectHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ProgressHolder) {
            final ProgressHolder progressHolder = (ProgressHolder) holder;
            progressHolder.progressBar.setVisibility(showFooter ? View.VISIBLE : View.GONE);

        } else {
            final OfferObjectHolder offerHolder = (OfferObjectHolder) holder;
            final OfferObject offerObject = dataset.get(position);

            offerHolder.title.setText(offerObject.getTitle());
            offerHolder.teaser.setText(offerObject.getTeaser());
            offerHolder.payout.setText(String.valueOf(offerObject.getPayout()));

            Picasso.with(offerHolder.thumbnail.getContext())
                    .load(offerObject.getThumbnail().getHires())
                    .into(offerHolder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        // if we have items, add one more row for the loader at the footer
        return null == this.dataset || this.dataset.isEmpty() ? 0 : this.dataset.size() + 1;
    }

    @Override
    public int getItemViewType(final int position) {
        // loader can only be at the last position
        return position != 0 && position == getItemCount() - 1 ? VIEWTYPE_LOADER : VIEWTYPE_ITEM;
    }

    public void setShowLoading(final boolean value) {
        showFooter = value;
    }

}
