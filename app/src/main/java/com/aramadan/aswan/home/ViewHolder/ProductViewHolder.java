package com.aramadan.aswan.home.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Interface.ItemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName_txt, productDesc_txt, productPrice_txt;
    public ImageView productImage;
    public ItemClickListener clickListener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImage = itemView.findViewById(R.id.item_product_img);
        productName_txt = itemView.findViewById(R.id.item_product_txt_Name);
        productDesc_txt = itemView.findViewById(R.id.item_product_txt_des);
        productPrice_txt= itemView.findViewById(R.id.item_product_txt_price);

    }

    public void setItemClickListener(ItemClickListener listener){
        this.clickListener = listener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v, getAdapterPosition(), false);
    }
}
