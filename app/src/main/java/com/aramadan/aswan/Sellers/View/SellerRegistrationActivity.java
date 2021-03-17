package com.aramadan.aswan.Sellers.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;
    private Button registerButton;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        nameInput = findViewById(R.id.seller_userName);
        emailInput = findViewById(R.id.seller_email);
        phoneInput = findViewById(R.id.seller_PhoneNumber);
        passwordInput = findViewById(R.id.seller_password);
        addressInput = findViewById(R.id.seller_address);
        registerButton = findViewById(R.id.seller_register_btn);
        progressBar = findViewById(R.id.reg_bar);

        mAuth = FirebaseAuth.getInstance();

        Button loginSellerBtn = findViewById(R.id.already_an_btn);
        loginSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(in);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });

    }

    private void registerSeller() {

        final String name = nameInput.getText().toString();
        final String phone = phoneInput.getText().toString();
        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        final String address = addressInput.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Please write your User Name.");
        }else if (TextUtils.isEmpty(email) || !email.matches(emailPattern)) {
            emailInput.setError("Invalid email address like ####@###.###");
        } else if (TextUtils.isEmpty(phone) || !(phone.length() == 11)) {
            phoneInput.setError("Invalid your Phone Number.");
        } else if (TextUtils.isEmpty(password) || !(password.length() >= 8)) {
            passwordInput.setError("You must have 8 characters in your password");
        }  else if (TextUtils.isEmpty(address)) {
            addressInput.setError("Please write your Address.");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.INVISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        final DatabaseReference rootRef;
                        rootRef = FirebaseDatabase.getInstance().getReference();

                        String uid = mAuth.getCurrentUser().getUid();

                        HashMap<String, Object> sellerMap = new HashMap<>();
                        sellerMap.put("uid", uid);
                        sellerMap.put("sellerPhone", phone);
                        sellerMap.put("sellerEmail", email);
                        sellerMap.put("sellerAddress", address);
                        sellerMap.put("sellerName", name);

                        rootRef.child("Sellers").child(uid).updateChildren(sellerMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        progressBar.setVisibility(View.INVISIBLE);
                                        registerButton.setVisibility(View.VISIBLE);

                                        Toast.makeText(SellerRegistrationActivity.this, "You are Registered Successfully ..", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    }
                                });

                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        registerButton.setVisibility(View.VISIBLE);
                        Toast.makeText(SellerRegistrationActivity.this, "You are Failed 2 ..", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }

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


















