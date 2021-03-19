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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.aramadan.aswan.R.string.AddedtoCartList;

public class ProductDetailsActivity extends AppCompatActivity {

    public static final String PRODUCT = "PRODUCT_DETAILS";
    private ImageView productImage;
    private TextView productName, productDes, productPrice;
    private Button addToCartBtn;
    private ElegantNumberButton numberButton;

    private String productID = "";

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private DatabaseReference sellerRef;

    private String sName, sAddress, sPhone, sEmail, uID;

    private Products productDetails;

    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        productDetails = new Gson().fromJson( getIntent().getStringExtra(PRODUCT),Products.class);

        initializeViews();
        getProductDetails();

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
        cartMap.put("pid", productDetails.getPid());
        cartMap.put("pname", productDetails.getPname());
        cartMap.put("price", productDetails.getPrice());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("image", productDetails.getImage());

        cartMap.put("sellerName", productDetails.getSellerName());
        cartMap.put("sellerAddress", productDetails.getSellerAddress());
        cartMap.put("sellerPhone", productDetails.getSellerPhone());
        cartMap.put("sellerEmail", productDetails.getSellerEmail());

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


    private void getProductDetails() {

        productName.setText(productDetails.getPname());
        productDes.setText(productDetails.getDescription());
        productPrice.setText(productDetails.getPrice());
        Picasso.get().load(productDetails.getImage()).into(productImage);

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