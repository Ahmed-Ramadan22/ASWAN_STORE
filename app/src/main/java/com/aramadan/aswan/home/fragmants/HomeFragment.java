package com.aramadan.aswan.home.fragmants;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aramadan.aswan.Admin.Ui.AdminLoginActivity;
import com.aramadan.aswan.Admin.Ui.AdminMaintainProducts;
import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Model.Products;
import com.aramadan.aswan.home.Ui.HomeActivity;
import com.aramadan.aswan.home.Ui.ProductDetailsActivity;
import com.aramadan.aswan.home.ViewHolder.OffersViewHolder;
import com.aramadan.aswan.home.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

import static android.view.View.GONE;


public class HomeFragment extends Fragment {

    private DatabaseReference productRef;
    private RecyclerView rVHomeProducts, rvOfferRef, rVMobileProducts,
            rvLaptopsProd, rvElectronicsProd, rvSportClothes,
            rvWatchesProd, rvWomanProd, rvEssentialsProd, rvRestaurantsProd,
            rvBeautyProd, rvHandMade;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_fragmant, container, false);

        // RecyclerView Product home
        rVHomeProducts = view.findViewById(R.id.recycler_home_products);
        rVHomeProducts.setHasFixedSize(true);
        rVHomeProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product beauty
        rvBeautyProd = view.findViewById(R.id.recycler_beauty_products);
        rvBeautyProd.setHasFixedSize(true);
        rvBeautyProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product OFFERS
        rvOfferRef = view.findViewById(R.id.recycler_offers_products);
        rvOfferRef.setHasFixedSize(true);
        rvOfferRef.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product Mobile & Tablets
        rVMobileProducts = view.findViewById(R.id.recycler_Mobiles_products);
        rVMobileProducts.setHasFixedSize(true);
        rVMobileProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product Laptops
        rvLaptopsProd = view.findViewById(R.id.recycler_laptops_products);
        rvLaptopsProd.setHasFixedSize(true);
        rvLaptopsProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product Electronics
        rvElectronicsProd = view.findViewById(R.id.recycler_electronics_products);
        rvElectronicsProd.setHasFixedSize(true);
        rvElectronicsProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product SportClothes
        rvSportClothes = view.findViewById(R.id.recycler_SportClothes_products);
        rvSportClothes.setHasFixedSize(true);
        rvSportClothes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product rvWatchesProd
        rvWatchesProd = view.findViewById(R.id.recycler_glasses_products);
        rvWatchesProd.setHasFixedSize(true);
        rvWatchesProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product woman_
        rvWomanProd = view.findViewById(R.id.recycler_woman_products);
        rvWomanProd.setHasFixedSize(true);
        rvWomanProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product Essentials
        rvEssentialsProd = view.findViewById(R.id.recycler_essentials_products);
        rvEssentialsProd.setHasFixedSize(true);
        rvEssentialsProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product restaurants
        rvRestaurantsProd = view.findViewById(R.id.recycler_restaurants_products);
        rvRestaurantsProd.setHasFixedSize(true);
        rvRestaurantsProd.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView Product restaurants
        rvHandMade = view.findViewById(R.id.recycler_handmade_products);
        rvHandMade.setHasFixedSize(true);
        rvHandMade.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        _beautyProduct();
        _manFashionProduct();
        _offersProduct();
        _mobileProducts();
        _laptopsProduct();
        _rvElectronicsProd();
        _rvSportClothes();
        _rvWatchesProd();
        _rvWomanProd();
        _rvEssentialsProd();
        _rvRestaurantsProd();
        _rvHandmadeProd();

    }

    private void _rvHandmadeProd() {
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Handmade"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {

                        if (!model.getProductState().equals("Approved")) {
                            holder.itemView.setVisibility(GONE);

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

        rvHandMade.setAdapter(adapter);
        adapter.startListening();
        /////////
    }

    private void _beautyProduct() {

        // rvBeautyProd RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Beauty"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
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

        rvBeautyProd.setAdapter(adapter);
        adapter.startListening();
        ////////
    }

    private void _manFashionProduct() {
        // CLOTHES RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Man's Fashion"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
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

        rVHomeProducts.setAdapter(adapter);
        adapter.startListening();
        /////////

    }

    private void _rvRestaurantsProd() {
        // Restaurants RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Restaurants"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
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

        rvRestaurantsProd.setAdapter(adapter);
        adapter.startListening();
        /////////
    }

    private void _rvEssentialsProd() {

        // Home Essentials RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Home Essentials"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
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

        rvEssentialsProd.setAdapter(adapter);
        adapter.startListening();
        /////////
    }

    private void _rvWomanProd() {
        // Women's Fashion RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Women's Fashion"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
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

        rvWomanProd.setAdapter(adapter);
        adapter.startListening();
        /////////
    }

    private void _rvWatchesProd() {
        // Watches RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Watches"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
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

        rvWatchesProd.setAdapter(adapter);
        adapter.startListening();
        /////////
    }

    private void _rvSportClothes() {
        // SportClothes RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Sports Clothes"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
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

        rvSportClothes.setAdapter(adapter);
        adapter.startListening();
        /////////
    }

    private void _rvElectronicsProd() {
        // Electronics RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Electronics & Accessories"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
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

        rvElectronicsProd.setAdapter(adapter);
        adapter.startListening();
        /////////

    }

    private void _laptopsProduct() {
        // Laptops RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Laptops"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
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

        rvLaptopsProd.setAdapter(adapter);
        adapter.startListening();
        /////////

    }

    private void _mobileProducts() {
        // Mobiles RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Mobile & Tablets"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
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

        rVMobileProducts.setAdapter(adapter);
        adapter.startListening();
        /////////
    }

    private void _offersProduct() {
        // Offers RECYCLER
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(productRef.orderByChild("category").equalTo("Offers"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, OffersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, OffersViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull OffersViewHolder holder, int position, @NonNull Products model) {

                        if (!model.getProductState().equals("Approved")) {
                            holder.itemView.setVisibility(GONE);

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
                    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.offars_item_cart,
                                        parent,
                                        false);

                        return new OffersViewHolder(view);
                    }
                };

        rvOfferRef.setAdapter(adapter);
        adapter.startListening();
        /////////
    }

}