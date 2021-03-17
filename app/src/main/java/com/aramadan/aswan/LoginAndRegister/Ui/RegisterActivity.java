package com.aramadan.aswan.LoginAndRegister.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.aramadan.aswan.LoginAndRegister.Models.Users;
import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Ui.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText email_sigUp_edt, phone_sigUp_edt, pass_sigUp_edt, userName_sigUp_edt;
    private ProgressDialog loadingBar;

    LinearLayout signInLay;

    private FirebaseAuth mAuth;
    DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_sigUp_edt = findViewById(R.id.email_edt_singUp_fg);
        phone_sigUp_edt = findViewById(R.id.Numper_edt_singUp_fg);
        pass_sigUp_edt = findViewById(R.id.password_edt_singUp_fg);
        userName_sigUp_edt = findViewById(R.id.userName_edt_singUp_fg);
        signInLay = findViewById(R.id.SignIn_lay);
        TextView backToSignIn = findViewById(R.id.go_to_signIn);


        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        Button signUp_btn = findViewById(R.id.SignUp_btn);

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

        // Back To Login Activity
        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


    }

    private void CreateAccount() {

        final String email = email_sigUp_edt.getText().toString();
        final String phone = phone_sigUp_edt.getText().toString();
        String password = pass_sigUp_edt.getText().toString();
        final String userName = userName_sigUp_edt.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(email) || !email.matches(emailPattern)) {
            email_sigUp_edt.setError("Invalid email address");
        } else if (TextUtils.isEmpty(phone) || !(phone.length() == 11)) {
            phone_sigUp_edt.setError("Invalid your Phone Number");
        } else if (TextUtils.isEmpty(password) || !(password.length() >= 8)) {
            pass_sigUp_edt.setError("You must have 8 characters in your password");
        } else if (TextUtils.isEmpty(userName)) {
            userName_sigUp_edt.setError("Please write your User Name.");
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, While we are checking the Credentials. ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        final DatabaseReference rootRef;
                        rootRef = FirebaseDatabase.getInstance().getReference();

                        String uid = mAuth.getCurrentUser().getUid();

                        HashMap<String, Object> sellerMap = new HashMap<>();
                        sellerMap.put("uid", uid);
                        sellerMap.put("phone", phone);
                        sellerMap.put("email", email);
                        sellerMap.put("name", userName);

                        myRootRef.child("Users")
                                .child(uid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Create Account has been Successfully.", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent in = new Intent(RegisterActivity.this, HomeActivity.class);
                                    startActivity(in);
                                }else {
                                    Toast.makeText(RegisterActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(RegisterActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
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
