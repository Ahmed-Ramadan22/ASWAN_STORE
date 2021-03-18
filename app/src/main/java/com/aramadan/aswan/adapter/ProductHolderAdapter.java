package com.aramadan.aswan.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Model.Products;
import com.aramadan.aswan.home.ViewHolder.OffersViewHolder;
import com.google.firebase.database.Query;


public class ProductHolderAdapter extends FirebaseRecyclerAdapter<OffersViewHolder, Products> {

    private final LayoutInflater inflater;
    private final Filter<Products> filter;
    private final ClickableAction<Products> viewAction;

    int layoutId;

    public ProductHolderAdapter(int layoutId,Query query, Context context,
                                   Filter<Products> filter,
                                   ClickableAction<Products> viewAction) {
        super(query);
        this.layoutId = layoutId;
        inflater = LayoutInflater.from(context);
        this.filter = filter;
        this.viewAction = viewAction;
    }

    @Override
    protected boolean validateItem(Products item) {
        return filter.filter(item);
    }


    @NonNull
    @Override
    public OffersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OffersViewHolder(inflater.inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {
        holder.init(getItem(position));
        holder.itemView.setOnClickListener(v -> viewAction.onClick(getItem(position)));
    }

    
}
