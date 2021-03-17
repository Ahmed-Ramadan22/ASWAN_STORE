package com.aramadan.aswan.Sellers.SellerFragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aramadan.aswan.Admin.Ui.AdminCheckNewProductActivity;
import com.aramadan.aswan.R;
import com.aramadan.aswan.Sellers.ViewHolder.ItemViewHolder;
import com.aramadan.aswan.home.Model.Products;
import com.aramadan.aswan.home.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeSellerFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference unVerifiedProductRef;


    public HomeSellerFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        unVerifiedProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home_seller, container, false);

        recyclerView = v.findViewById(R.id.home_seller_rey);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(v.getContext(), 2));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unVerifiedProductRef.orderByChild("uid")
                                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products,ItemViewHolder >(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder  holder, int position, @NonNull Products model) {

                        holder.productName_txt.setText(model.getPname());
                        holder.productDesc_txt.setText(model.getDescription());
                        holder.productPrice_txt.setText(model.getPrice() );
                        holder.productState_txt.setText(model.getProductState());
                        Picasso.get().load(model.getImage()).into(holder.productImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String productID = model.getPid();
                                CharSequence options[] = new CharSequence[] {

                                        "Yes",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Do you want to Delete this Product. Are you Sure?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int position) {

                                        if (position == 0){
                                            DeleteProduct(productID);
                                        }
                                        if (position == 1){

                                        }

                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.seller_item_view,
                                        parent,
                                        false);

                        return new ItemViewHolder(view);
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    private void DeleteProduct(String productID) {

        unVerifiedProductRef.child(productID)
               .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "That item has been Deleted.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}