package com.aramadan.aswan.home.Ui;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.sax.ElementListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aramadan.aswan.LoginAndRegister.Models.Users;
import com.aramadan.aswan.LoginAndRegister.Prevalent.Prevalent;
import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Model.Products;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.aramadan.aswan.R.string.AddedtoCartList;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productDes, productPrice;
    private Button addToCartBtn;
    private ElegantNumberButton numberButton;

    private String productID = "";

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;
    private Products products;

    private DatabaseReference sellerRef;

    private String sName, sAddress, sPhone, sEmail, uID;

    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        initializeViews();
        getProductDetails(productID);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        addingToCartList();
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProductDetailsActivity.this);
        String sellerUid = preferences.getString("seller_id","");

        sellerRef.child(sellerUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            sName = snapshot.child("sellerName").getValue().toString();
                            sAddress = snapshot.child("sellerAddress").getValue().toString();
                            sPhone = snapshot.child("sellerPhone").getValue().toString();
                            uID = snapshot.child("uid").getValue().toString();
                            sEmail = snapshot.child("sellerEmail").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void addingToCartList() {
        progressBar.setVisibility(View.VISIBLE);
        addToCartBtn.setVisibility(View.INVISIBLE);


        String saveCurrentDate, saveCurrentTime;

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        String userN = mAuth.getCurrentUser().getUid();

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", products.getPname());
        cartMap.put("price", products.getPrice());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("image", products.getImage());

        cartMap.put("sellerName", sName);
        cartMap.put("sellerAddress", sAddress);
        cartMap.put("sellerPhone", sPhone);
        cartMap.put("sellerEmail", sEmail);
        cartMap.put("uid", uID);


        cartListRef.child("User View").child(userN)
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            cartListRef.child("Admin View").child(userN)
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            addToCartBtn.setVisibility(View.VISIBLE);

                                            Toast.makeText(ProductDetailsActivity.this, AddedtoCartList, Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ProductDetailsActivity.this, HomeActivity.class));
                                        }
                                    });
                        }

                    }
                });

    }

    private void getProductDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    products = snapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productDes.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initializeViews() {

        mAuth = FirebaseAuth.getInstance();

        productImage = findViewById(R.id.product_image_details);
        productName = findViewById(R.id.product_name_details);
        productDes = findViewById(R.id.product_des_details);
        productPrice = findViewById(R.id.product_price_details);
        addToCartBtn = findViewById(R.id.add_toCart_btn);
        numberButton = findViewById(R.id.number_counter_btn);
        progressBar = findViewById(R.id.progressBar_cart);

    }


    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}