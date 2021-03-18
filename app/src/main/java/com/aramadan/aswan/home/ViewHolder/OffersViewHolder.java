package com.aramadan.aswan.home.ViewHolder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Interface.ItemClickListener;
import com.aramadan.aswan.home.Model.Products;
import com.squareup.picasso.Picasso;

import static android.view.View.GONE;

public class OffersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



    public TextView productName_txt, productDesc_txt, productPrice_txt;
    public ImageView productImage;
    public ItemClickListener clickListener;

    public OffersViewHolder(@NonNull View itemView) {
        super(itemView);

        productImage = itemView.findViewById(R.id.offers_cart_img_product);
        productName_txt = itemView.findViewById(R.id.offers_name_cart_item);
        productDesc_txt = itemView.findViewById(R.id.offers_product_txt_des);
        productPrice_txt= itemView.findViewById(R.id.offers_price_cart_item);


    }

    public void setItemClickListener(ItemClickListener listener){
        this.clickListener = listener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v, getAdapterPosition(), false);
    }

    public void init(Products item) {
        
            itemView.setVisibility(View.VISIBLE);

            productName_txt.setText(item.getPname());
            productDesc_txt.setText(item.getDescription());
            productPrice_txt.setText(item.getPrice());
            Picasso.get().load(item.getImage()).into(productImage);

    }


}
