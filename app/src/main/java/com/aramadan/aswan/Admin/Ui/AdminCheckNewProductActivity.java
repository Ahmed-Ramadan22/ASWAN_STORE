package com.aramadan.aswan.Admin.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Interface.ItemClickListener;
import com.aramadan.aswan.home.Model.Products;
import com.aramadan.aswan.home.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class AdminCheckNewProductActivity extends AppCompatActivity {

    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private RecyclerView recyclerView;
    private DatabaseReference unVerifiedProductRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_product);

        unVerifiedProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.admin_products_checklist);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unVerifiedProductRef.orderByChild("productState").equalTo("Not Approved"), Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {


                        holder.productName_txt.setText(model.getPname());
                        holder.productDesc_txt.setText(model.getDescription());
                        holder.productPrice_txt.setText(model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.productImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String productID = model.getPid();
                                CharSequence options[] = new CharSequence[]{

                                        "Yes",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewProductActivity.this);
                                builder.setTitle(R.string.builderAbrove);
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int position) {

                                        if (position == 0) {
                                            changeProductState(productID);
                                        }
                                        if (position == 1) {

                                        }

                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.product_items_layout,
                                        parent,
                                        false);

                        return new ProductViewHolder(view);
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void changeProductState(String productID) {

        unVerifiedProductRef.child(productID)
                .child("productState")
                .setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AdminCheckNewProductActivity.this, R.string.toastAprove, Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}




















