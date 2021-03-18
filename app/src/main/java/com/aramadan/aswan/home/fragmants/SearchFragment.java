package com.aramadan.aswan.home.fragmants;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */

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
import com.aramadan.aswan.adapter.ClickableAction;
import com.aramadan.aswan.adapter.Filter;
import com.aramadan.aswan.adapter.ProductHolderAdapter;
import com.aramadan.aswan.home.Model.Products;
import com.aramadan.aswan.home.Ui.ProductDetailsActivity;
import com.aramadan.aswan.home.ViewHolder.CartViewHolder;
import com.aramadan.aswan.home.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import static android.view.View.GONE;


public class SearchFragment extends Fragment {

    private EditText inputSearch;
    private ImageView searchBtn;
    private RecyclerView searchList;
    private DatabaseReference mUserDatabase;;

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

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Products");

        inputSearch = v.findViewById(R.id.search_edit_product);
        searchBtn = v.findViewById(R.id.search_img_btn);
        searchList = v.findViewById(R.id.search_list_ry);

        searchList.setLayoutManager(new LinearLayoutManager(getContext()));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = inputSearch.getText().toString();

                firebaseUserSearch(searchText);

            }
        });

        return v;
    }

    private void firebaseUserSearch(String searchText) {

        Query firebaseSearchQuery = mUserDatabase.orderByChild("pname").startAt(searchText).endAt(searchText + "\uf8ff");


        searchList.setAdapter(new ProductHolderAdapter(
                R.layout.offars_item_cart,
                firebaseSearchQuery,
                getActivity(),
                new Filter<Products>() {
                    @Override
                    public boolean filter(Products products) {
                        return  products.getProductState().equals("Approved");
                    }
                },
                new ClickableAction<Products>() {
                    @Override
                    public void onClick(Products item) {
                        Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                        intent.putExtra("pid", item.getPid());
                        startActivity(intent);
                    }
                }));

    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    }
}