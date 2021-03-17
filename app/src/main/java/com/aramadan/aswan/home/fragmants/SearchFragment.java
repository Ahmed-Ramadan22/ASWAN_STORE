package com.aramadan.aswan.home.fragmants;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Model.Products;
import com.aramadan.aswan.home.Ui.ProductDetailsActivity;
import com.aramadan.aswan.home.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static android.view.View.GONE;


public class SearchFragment extends Fragment {

    private EditText inputSearch;
    private ImageView searchBtn;
    private RecyclerView searchList;

    private String searchInput;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search, container, false);

        inputSearch = v.findViewById(R.id.search_edit_product);
        searchBtn = v.findViewById(R.id.search_img_btn);
        searchList = v.findViewById(R.id.search_list_ry);

        searchList.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = inputSearch.getText().toString();

                onStart();

            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").startAt(searchInput).endAt(searchInput + "\ufBff"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {

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
                            Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);

                        }
                    });
                }


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

        searchList.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    }
}