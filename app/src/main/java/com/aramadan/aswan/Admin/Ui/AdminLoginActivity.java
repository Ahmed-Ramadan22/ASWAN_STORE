package com.aramadan.aswan.Admin.Ui;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aramadan.aswan.LoginAndRegister.Models.Users;
import com.aramadan.aswan.LoginAndRegister.Prevalent.Prevalent;
import com.aramadan.aswan.LoginAndRegister.Ui.LoginActivity;
import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Ui.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.aramadan.aswan.R.string.sucess;

public class AdminLoginActivity extends AppCompatActivity {

    public  static final String USER_TYPE = "uesrtype";
    private EditText email_admin_edt, password_admin_edt, userName_admin_edt;
    private ProgressDialog loadingBar;
    private Button adminLogin;

    private TextView go_to_signIn;
    private ProgressDialog dialogLoading;
    private FirebaseAuth mAuth;

    LinearLayout notAdmin_lay;
    private String parentDbName = "Admins";


    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        email_admin_edt = findViewById(R.id.email_admin_edt);
        password_admin_edt = findViewById(R.id.password_admin_edt);
        userName_admin_edt = findViewById(R.id.userName_admin_edt);
        notAdmin_lay = findViewById(R.id.notAdmin_lay);
        go_to_signIn = findViewById(R.id.go_to_signIn);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        adminLogin = findViewById(R.id.login_admin_btn);
        dialogLoading = new ProgressDialog(this);

        // Back To Login Activity
        go_to_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLoginActivity.this, LoginActivity.class));
                finish();
            }
        });

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessAdmin();
            }
        });

    }

    private void accessAdmin() {

        final String email = email_admin_edt.getText().toString();
        final String password = password_admin_edt.getText().toString();
        final String userName = userName_admin_edt.getText().toString();

        if (TextUtils.isEmpty(password) || !(password.length() >=8)){
            password_admin_edt.setError(getString(R.string.desPass));
            password_admin_edt.requestFocus();
            return;
        }else  if (TextUtils.isEmpty(email) ||!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            email_admin_edt.setError(getString(R.string.validEmail));
            email_admin_edt.requestFocus();
            return;
        } else {

            dialogLoading.setTitle(getString(R.string.loginAdmin));
            dialogLoading.setMessage(getString(R.string.checking));
            dialogLoading.setCanceledOnTouchOutside(false);
            dialogLoading.show();

            AllowAccessToAccount(email,  password, userName );

        }
    }

    private void AllowAccessToAccount(final String email, final String password, final String userName ) {

        final DatabaseReference RootRof;
        RootRof = FirebaseDatabase.getInstance().getReference();

        RootRof.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(parentDbName).child(userName).exists()){
                    Users usersData = snapshot.child(parentDbName).child(userName).getValue(Users.class);

                    if (usersData.getEmail().equals(email) || usersData.getPassword().equals(password) ){
                            if (parentDbName.equals("Admins")){

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AdminLoginActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putInt(USER_TYPE, 1);
                                editor.apply();
                                
                                Toast.makeText(AdminLoginActivity.this, sucess, Toast.LENGTH_SHORT).show();
                                dialogLoading.dismiss();
                                startActivity(new Intent(AdminLoginActivity.this, AdminHomeActivity.class));
                            }
                    }else {
                        email_admin_edt.setError(getString(R.string.InCorrect_Email_and_Password));
                    }

                } else {
                    Toast.makeText(AdminLoginActivity.this, R.string.tostWrong, Toast.LENGTH_SHORT).show();
                    dialogLoading.dismiss();
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