package com.aramadan.aswan.Admin.Ui;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aramadan.aswan.Admin.Model.AdminCartViewHolder;
import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Model.Cart;
import com.aramadan.aswan.home.Model.Products;
import com.aramadan.aswan.home.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminUserProductActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private Products productsDe;

    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_proudact);

        String userID = getIntent().getStringExtra("uid");

        productsList = findViewById(R.id.userProducts_list_ry);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);

        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(userID).child("Products");
    }

    @Override
    protected void onStart() {

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(cartListRef, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, AdminCartViewHolder> adapter = new FirebaseRecyclerAdapter<Products, AdminCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminCartViewHolder holder, int position, @NonNull Products model) {

                holder.txtProductName.setText(model.getPname());
                holder.txtProductQuantity.setText(model.getQuantity());
                holder.txtProductPrice.setText(model.getPrice());
                holder.txtSellerName.setText(model.getSellerName());
                holder.txtSellerPhone.setText(model.getSellerPhone());
                holder.txtSellerAddress.setText(model.getSellerAddress());
                Picasso.get().load(model.getImage()).into(holder.imgProduct);

            }

            @NonNull
            @Override
            public AdminCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.admin_cart_item_layout,
                                parent,
                                false);

                return new AdminCartViewHolder(view);
            }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}