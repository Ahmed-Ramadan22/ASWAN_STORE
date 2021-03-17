package com.aramadan.aswan.Admin.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProducts extends AppCompatActivity {

    private Button applyChangesBtn, deleteProductBtn;
    private EditText nameEdt, priceEdt, descriptionEdt;
    private ImageView imageViewProduct;

    private String productID = "";

    private DatabaseReference productRef;
    private ProgressBar progressBar , deleteProgressBar;

    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_maintain_products);

        productID  = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        imageViewProduct = findViewById(R.id.product_image_maintain);
        descriptionEdt = findViewById(R.id.product_description_maintain);
        priceEdt = findViewById(R.id.product_price_maintain);
        nameEdt = findViewById(R.id.product_name_maintain);
        applyChangesBtn = findViewById(R.id.updateChange_maintain_btn);
        progressBar = findViewById(R.id.progressBar_addPro);
        deleteProductBtn = findViewById(R.id.deleteProduct_maintain_btn);
        deleteProgressBar= findViewById(R.id.progressBar_delete);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProductAdmin();
            }
        });

    }

    private void deleteProductAdmin() {
        deleteProgressBar.setVisibility(View.VISIBLE);
        deleteProductBtn.setVisibility(View.INVISIBLE);

        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deleteProgressBar.setVisibility(View.INVISIBLE);
                deleteProductBtn.setVisibility(View.VISIBLE);

                Intent intent = new Intent(AdminMaintainProducts.this , AdminEditDeleteProductActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainProducts.this, "The Product is Deleted Successfully.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void applyChanges() {

        progressBar.setVisibility(View.VISIBLE);
        applyChangesBtn.setVisibility(View.INVISIBLE);

        String Name  = nameEdt.getText().toString();
        String Price = priceEdt.getText().toString();
        String Desc  = descriptionEdt.getText().toString();

        if (Name.equals("")){
            nameEdt.setError("Input Product Name .. ");
        } else  if (Price.equals("")){
            nameEdt.setError("Input Product Price ..");
        } else  if (Desc.equals("")){
            nameEdt.setError("Input Product Description ..");
        } else {
            HashMap <String, Object> productMap = new HashMap<>();

            productMap.put("pid", productID);
            productMap.put("description", Desc);
            productMap.put("price", Price);
            productMap.put("pname", Name);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainProducts.this, "Changes Applied Successfully.", Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);
                        applyChangesBtn.setVisibility(View.VISIBLE);

                        Intent intent = new Intent(AdminMaintainProducts.this , AdminEditDeleteProductActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            });

        }
    }

    private void displaySpecificProductInfo() {

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDesc = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();

                    nameEdt.setText(pName);
                    priceEdt.setText(pPrice);
                    descriptionEdt.setText(pDesc);
                    Picasso.get().load(pImage).into(imageViewProduct);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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