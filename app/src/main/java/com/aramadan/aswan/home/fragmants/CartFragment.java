package com.aramadan.aswan.home.fragmants;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Model.Cart;
import com.aramadan.aswan.home.Ui.ConfirmFinalOrder;
import com.aramadan.aswan.home.Ui.ProductDetailsActivity;
import com.aramadan.aswan.home.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button nextProcessBtn, cartCalBtn;
    private TextView txtTotalAmount;
    private FirebaseAuth mAuth;

    private int overTotalPrice = 0;

    public CartFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart_fragmant, container, false);

        nextProcessBtn = v.findViewById(R.id.cart_next_btn);
        txtTotalAmount = v.findViewById(R.id.total_Amount_cart_txt);
        cartCalBtn = v.findViewById(R.id.cart_cal_btn);

        recyclerView = v.findViewById(R.id.cart_list_ry);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // calculate Amount of product
        cartCalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTotalAmount.setText(String.valueOf(overTotalPrice));
            }
        });

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConfirmFinalOrder.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        String userN = mAuth.getCurrentUser().getUid();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(userN).child("Products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                holder.txtProductName.setText(model.getPname());
                holder.txtProductQuantity.setText(model.getQuantity());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imgProduct);

                int oneTypeProductTPrice = Integer.parseInt(model.getPrice()) * Integer.parseInt(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductTPrice;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence[] options = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (i == 1) {
                                    cartListRef.child("User View")
                                            .child(userN)
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Item Removed Successfully ..", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                     });
                                }
                            }
                        });

                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

}
















