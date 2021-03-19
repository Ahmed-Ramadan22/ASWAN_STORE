package com.aramadan.aswan.home.fragmants;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aramadan.aswan.R;
import com.aramadan.aswan.adapter.ClickableAction;
import com.aramadan.aswan.adapter.Filter;
import com.aramadan.aswan.adapter.ProductHolderAdapter;
import com.aramadan.aswan.home.Model.Products;
import com.aramadan.aswan.home.Ui.ProductDetailsActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;


public class HomeFragment extends Fragment {

    private DatabaseReference productRef;
    private RecyclerView rVHomeProducts, rvOfferRef, rVMobileProducts,
            rvLaptopsProd, rvElectronicsProd, rvSportClothes,
            rvWatchesProd, rvWomanProd, rvEssentialsProd, rvRestaurantsProd,
            rvBeautyProd, rvHandMade;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_fragmant, container, false);

        // RecyclerView Product home
        rVHomeProducts = view.findViewById(R.id.recycler_home_products);
        rVHomeProducts.setHasFixedSize(true);
        rVHomeProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product beauty
        rvBeautyProd = view.findViewById(R.id.recycler_beauty_products);
        rvBeautyProd.setHasFixedSize(true);
        rvBeautyProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product OFFERS
        rvOfferRef = view.findViewById(R.id.recycler_offers_products);
        rvOfferRef.setHasFixedSize(true);
        rvOfferRef.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product Mobile & Tablets
        rVMobileProducts = view.findViewById(R.id.recycler_Mobiles_products);
        rVMobileProducts.setHasFixedSize(true);
        rVMobileProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product Laptops
        rvLaptopsProd = view.findViewById(R.id.recycler_laptops_products);
        rvLaptopsProd.setHasFixedSize(true);
        rvLaptopsProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product Electronics
        rvElectronicsProd = view.findViewById(R.id.recycler_electronics_products);
        rvElectronicsProd.setHasFixedSize(true);
        rvElectronicsProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product SportClothes
        rvSportClothes = view.findViewById(R.id.recycler_SportClothes_products);
        rvSportClothes.setHasFixedSize(true);
        rvSportClothes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product rvWatchesProd
        rvWatchesProd = view.findViewById(R.id.recycler_glasses_products);
        rvWatchesProd.setHasFixedSize(true);
        rvWatchesProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product woman_
        rvWomanProd = view.findViewById(R.id.recycler_woman_products);
        rvWomanProd.setHasFixedSize(true);
        rvWomanProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product Essentials
        rvEssentialsProd = view.findViewById(R.id.recycler_essentials_products);
        rvEssentialsProd.setHasFixedSize(true);
        rvEssentialsProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product restaurants
        rvRestaurantsProd = view.findViewById(R.id.recycler_restaurants_products);
        rvRestaurantsProd.setHasFixedSize(true);
        rvRestaurantsProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // recycler_handmade_products
        rvHandMade = view.findViewById(R.id.recycler_handmade_products);
        rvHandMade.setHasFixedSize(true);
        rvHandMade.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        _beautyProduct();
        _manFashionProduct();
        _offersProduct();
        _mobileProducts();
        _laptopsProduct();
        _rvElectronicsProd();
        _rvSportClothes();
        _rvWatchesProd();
        _rvWomanProd();
        _rvEssentialsProd();
        _rvRestaurantsProd();
        _rvHandmadeProd();

    }



    private void onClick(Products products) {

        Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
        intent.putExtra("pid", products.getPid());
        intent.putExtra(ProductDetailsActivity.PRODUCT,new Gson().toJson(products));
        startActivity(intent);


    }

    private void _rvHandmadeProd() {

        rvHandMade.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Handmade") && products.getProductState().equals("Approved");
                    }
                },this::onClick
            ));
    }




    private void _beautyProduct() {

        rvBeautyProd.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Beauty") && products.getProductState().equals("Approved");
                    }
                },this::onClick));

    }

    private void _manFashionProduct() {

        rVHomeProducts.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Man's Fashion") && products.getProductState().equals("Approved");
                    }
                }, this::onClick));
    }

    private void _rvRestaurantsProd() {

        rvRestaurantsProd.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Restaurants") && products.getProductState().equals("Approved");
                    }
                },this::onClick));
    }

    private void _rvEssentialsProd() {

        rvEssentialsProd.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Home Essentials") && products.getProductState().equals("Approved");
                    }
                },
                this::onClick));

    }

    private void _rvWomanProd() {

        rvWomanProd.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Women's Fashion") && products.getProductState().equals("Approved");
                    }
                },
                this::onClick));
    }

    private void _rvWatchesProd() {

        rvWatchesProd.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Watches") && products.getProductState().equals("Approved");
                    }
                },
                this::onClick));
    }

    private void _rvSportClothes() {

        rvSportClothes.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Sports Clothes") && products.getProductState().equals("Approved");
                    }
                },
                this::onClick));

    }

    private void _rvElectronicsProd() {

        rvElectronicsProd.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Electronics & Accessories") && products.getProductState().equals("Approved");
                    }
                },
                this::onClick));
    }

    private void _laptopsProduct() {
        rvLaptopsProd.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Laptops") && products.getProductState().equals("Approved");
                    }
                },
                this::onClick));

    }

    private void _mobileProducts() {

        rVMobileProducts.setAdapter(new ProductHolderAdapter(
                R.layout.product_items_layout,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Mobile & Tablets") && products.getProductState().equals("Approved");
                    }
                },
                this::onClick));

    }

    private void _offersProduct() {

        rvOfferRef.setAdapter(new ProductHolderAdapter(
                R.layout.offars_item_cart,
                productRef,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return products.getCategory().equals("Offers") && products.getProductState().equals("Approved");
                    }
                },
                this::onClick));

    }

}