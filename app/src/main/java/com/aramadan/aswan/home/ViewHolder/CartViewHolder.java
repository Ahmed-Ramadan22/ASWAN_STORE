package com.aramadan.aswan.home.ViewHolder;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Interface.ItemClickListener;
import android.view.View;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    public ImageView imgProduct;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.product_name_cart_item);
        txtProductPrice = itemView.findViewById(R.id.product_total_cart_item);
        txtProductQuantity = itemView.findViewById(R.id.product_quantity_cart_item);
        imgProduct = itemView.findViewById(R.id.cart_img_product);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition() , false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }
}
