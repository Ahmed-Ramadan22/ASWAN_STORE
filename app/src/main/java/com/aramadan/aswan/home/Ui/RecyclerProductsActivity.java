package com.aramadan.aswan.home.Ui;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.aramadan.aswan.R;
import com.aramadan.aswan.adapter.ProductHolderAdapter;
import com.aramadan.aswan.home.Model.Products;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerProductsActivity extends AppCompatActivity {

    private DatabaseReference productRef;
    private RecyclerView ryProductsCategory;
    private String CategoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_products);

        CategoryName = getIntent().getExtras().get("Category").toString();
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");


        ryProductsCategory = findViewById(R.id.recycler_products_Category);
        ryProductsCategory.setHasFixedSize(true);
        ryProductsCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onStart() {
        super.onStart();

        ryProductsCategory.setAdapter(new ProductHolderAdapter(
                R.layout.offars_item_cart,
                productRef,
                this,
                this::isValidProduct,
                this::selectedProduct));

    }

    private void selectedProduct(Products products) {
                   Intent intent = new Intent(RecyclerProductsActivity.this, ProductDetailsActivity.class);
                    intent.putExtra("pid", products.getPid());
                    startActivity(intent);

    }

    private boolean isValidProduct(Products products) {
        return products.getCategory().equals(CategoryName)&&products.getProductState().equals("Approved");


    }
}