package com.aramadan.aswan.Sellers.View;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aramadan.aswan.LoginAndRegister.Ui.ForgetPasswordActivity;
import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.aramadan.aswan.R.string.Successfully;

public class SellerLoginActivity extends AppCompatActivity {

    private Button loginSellerBtn;
    private EditText emailInput, passwordInput;
    private ProgressBar bar;

    private FirebaseAuth mAuth;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        loginSellerBtn = findViewById(R.id.seller_login_btn);
        emailInput = findViewById(R.id.seller_email_log);
        passwordInput = findViewById(R.id.seller_password_log);
        bar = findViewById(R.id.login_bar);

        mAuth = FirebaseAuth.getInstance();


        loginSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });


    }

    private void loginSeller() {

        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();



        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(email) || !email.matches(emailPattern)) {
            emailInput.setError(getString(R.string.Invalidemailaddresslike));
        } else if (TextUtils.isEmpty(password) || !(password.length() >= 8)) {
            passwordInput.setError(getString(R.string.characters));
        } else {
            bar.setVisibility(View.VISIBLE);
            loginSellerBtn.setVisibility(View.INVISIBLE);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        String uid = mAuth.getCurrentUser().getUid();

                        bar.setVisibility(View.INVISIBLE);
                        loginSellerBtn.setVisibility(View.VISIBLE);

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SellerLoginActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("seller_id", uid);
                        editor.apply();

                        Toast.makeText(SellerLoginActivity.this, Successfully, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        bar.setVisibility(View.INVISIBLE);
                        loginSellerBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(SellerLoginActivity.this, R.string.TryAgainandcheck, Toast.LENGTH_SHORT).show();
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

    public void callForgetPasswordSeller(View view) {
        startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));

    }
}