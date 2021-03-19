package com.aramadan.aswan.Sellers.View;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aramadan.aswan.Admin.Ui.AdminLoginActivity;
import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Ui.HomeActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static com.aramadan.aswan.R.string.mandatory;

public class SellerAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, ProdName, saveCurrentDate, saveCurrentTime;

    private ImageView addImageProduct;
    private EditText inputNameProduct, inputDescProduct, inputPriceProduct;
    private Button addNewProductBtn;
    private static final int GalleryPick = 1;
    private Uri ImageUri;

    private String productRandomKey, downloadImageUri;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef, sellerRef;
    private ProgressBar progressBar;

    private String sName, sAddress, sPhone, sEmail, uID;

    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);


        CategoryName = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("Category")).toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        addImageProduct = findViewById(R.id.seller_add_img_product);
        inputNameProduct = findViewById(R.id.seller_add_product_name_edt);
        inputDescProduct = findViewById(R.id.seller_add_product_desc_edt);
        inputPriceProduct = findViewById(R.id.seller_add_product_price_edt);
        addNewProductBtn = findViewById(R.id.seller_add_product_btn);
        progressBar = findViewById(R.id.seller_progressBar_addPro);

        addImageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SellerAddNewProductActivity.this);
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


    private void ValidateProductData() {

        Description = inputDescProduct.getText().toString();
        Price = inputPriceProduct.getText().toString();
        ProdName = inputNameProduct.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(this, mandatory, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description)) {
            inputDescProduct.setError(getString(R.string.InputDescription));
        } else if (TextUtils.isEmpty(Price)) {
            inputPriceProduct.setError(getString(R.string.InputPrice));
        } else if (TextUtils.isEmpty(ProdName)) {
            inputNameProduct.setError(getString(R.string.InputName));
        } else {
            StoreProductInformation();
        }

    }

    private void StoreProductInformation() {

        progressBar.setVisibility(View.VISIBLE);
        addNewProductBtn.setVisibility(View.INVISIBLE);

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
                addNewProductBtn.setVisibility(View.VISIBLE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.GONE);
                addNewProductBtn.setVisibility(View.VISIBLE);

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }

                        downloadImageUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUri = task.getResult().toString();

                                    SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUri);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", ProdName);

        productMap.put("sellerName", sName);
        productMap.put("sellerAddress", sAddress);
        productMap.put("sellerPhone", sPhone);
        productMap.put("sellerEmail", sEmail);
        productMap.put("uid", uID);

        productMap.put("productState", "Not Approved");

        productsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            addNewProductBtn.setVisibility(View.VISIBLE);
                            addNewProductBtn.setText("Done");
                            Intent in = new Intent(SellerAddNewProductActivity.this, SellerHomeActivity.class);
                            startActivity(in);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            addNewProductBtn.setVisibility(View.VISIBLE);
                            String message = task.getException().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            addImageProduct.setImageURI(ImageUri);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
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