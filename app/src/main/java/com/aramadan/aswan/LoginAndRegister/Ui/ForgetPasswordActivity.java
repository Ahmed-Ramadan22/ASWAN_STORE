package com.aramadan.aswan.LoginAndRegister.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
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

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private Button reset_Pass;
    private EditText inputEmail;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        reset_Pass = findViewById(R.id.reset_pass_btn);
        inputEmail = findViewById(R.id.inputMobile);

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        reset_Pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty()){
            inputEmail.setError("Email is required!");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Please provide valid email!");
            inputEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        reset_Pass.setVisibility(View.INVISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    reset_Pass.setVisibility(View.VISIBLE);
                    Toast.makeText(ForgetPasswordActivity.this, "Check your email to reset password ..", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ForgetPasswordActivity.this , LoginActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    progressBar.setVisibility(View.GONE);
                    reset_Pass.setVisibility(View.VISIBLE);
                    Toast.makeText(ForgetPasswordActivity.this, "Try Again! Something wrong happened ..", Toast.LENGTH_SHORT).show();
                }

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









