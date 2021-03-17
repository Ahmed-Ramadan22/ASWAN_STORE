package com.aramadan.aswan.home.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Model.Products;
import com.aramadan.aswan.home.ViewHolder.OffersViewHolder;
import com.aramadan.aswan.home.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.view.View.GONE;

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

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo(CategoryName), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, OffersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, OffersViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull OffersViewHolder holder, int position, @NonNull Products model) {

                        if (!model.getProductState().equals("Approved")) {
                            holder.itemView.setVisibility(GONE);
                            //  Toast.makeText(getContext(), "GONE", Toast.LENGTH_SHORT).show();
                        } else {
                            holder.itemView.setVisibility(View.VISIBLE);

                            holder.productName_txt.setText(model.getPname());
                            holder.productDesc_txt.setText(model.getDescription());
                            holder.productPrice_txt.setText(model.getPrice());
                            Picasso.get().load(model.getImage()).into(holder.productImage);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(RecyclerProductsActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                                }
                            });
                        }

                    }

                    @NonNull
                    @Override
                    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.offars_item_cart,
                                        parent,
                                        false);

                        return new OffersViewHolder(view);
                    }
                };

        ryProductsCategory.setAdapter(adapter);
        adapter.startListening();
        ////////

    }
}