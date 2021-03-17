package com.aramadan.aswan.Sellers.SellerFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aramadan.aswan.R;
import com.aramadan.aswan.Sellers.View.SellerAddNewProductActivity;


public class SellerCategoryFragment extends Fragment {

    private CardView mobileTablaCard, manFashionCard, laptopsCard,
            electronicCard, sportClothesCard, womanFashionCard, glassesCard,
            homeCard, offerCart, restaurantCart,beautyCart, handmadeCart;

    public SellerCategoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_category_seller, container, false);

        offerCart    = v.findViewById(R.id.seller_offers_cart_frag);
        restaurantCart    = v.findViewById(R.id.seller_Restaurants_cart_frag);
        beautyCart = v.findViewById(R.id.seller_beauty_cart_frag);
        mobileTablaCard  = v.findViewById(R.id.seller_mobile_card_frag);
        manFashionCard   = v.findViewById(R.id.seller_man_Fashion_cart_frag);
        laptopsCard      = v.findViewById(R.id.seller_laptop_frag);
        electronicCard = v.findViewById(R.id.seller_electronic_cart_frag);
        sportClothesCard = v.findViewById(R.id.seller_sport_clothes_cart_frag);
        womanFashionCard = v.findViewById(R.id.seller_womens_fashion_cart_frag);
        glassesCard      = v.findViewById(R.id.seller_glasses_cart_frag);
        homeCard         = v.findViewById(R.id.seller_home_cart_frag);
        handmadeCart     = v.findViewById(R.id.seller_handmade_cart_frag);

        cartAction();

        return v;
    }

    private void cartAction() {

        beautyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Beauty");
                startActivity(intent);
            }
        });

        offerCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Offers");
                startActivity(intent);
            }
        });

        restaurantCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Restaurants");
                startActivity(intent);
            }
        });

        mobileTablaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Mobile & Tablets");
                startActivity(intent);
            }
        });

        manFashionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Man's Fashion");
                startActivity(intent);
            }
        });

        laptopsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Laptops");
                startActivity(intent);
            }
        });

        electronicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Electronics & Accessories");
                startActivity(intent);
            }
        });

        sportClothesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Sports Clothes");
                startActivity(intent);
            }
        });

        womanFashionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Women's Fashion");
                startActivity(intent);
            }
        });

        glassesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Watches");
                startActivity(intent);
            }
        });

        homeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Home Essentials");
                startActivity(intent);
            }
        });

        handmadeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Handmade");
                startActivity(intent);
            }
        });


    }
}