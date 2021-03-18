package com.aramadan.aswan.LoginAndRegister.Ui;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aramadan.aswan.Admin.Ui.AdminLoginActivity;
import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.home.Ui.HomeActivity;
import com.aramadan.aswan.LoginAndRegister.Prevalent.Prevalent;
import com.aramadan.aswan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

import static com.aramadan.aswan.Admin.Ui.AdminLoginActivity.USER_TYPE;
import static com.aramadan.aswan.R.string.Failedtologin;

public class LoginActivity extends AppCompatActivity {

    private EditText email_edt_log, pass_edt_log;
    private TextView goToSignUp , admin_txt;
    private Button login_btn;

    private ProgressDialog dialogLoading;
    private FirebaseAuth mAuth;

    private CheckBox checkbox_rm;

    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_edt_log = findViewById(R.id.email_edt_login_fg);
        pass_edt_log  = findViewById(R.id.password_edt_login_fg);
        login_btn     = findViewById(R.id.login_btn);

        admin_txt  = findViewById(R.id.login_admin);
        goToSignUp    = findViewById(R.id.go_to_signUp);

        checkbox_rm = findViewById(R.id.checkbox_rm);
        Paper.init(this);

        dialogLoading = new ProgressDialog(this);
        Paper.init(this);
        mAuth = FirebaseAuth.getInstance();

        //Go to Register Activity to create Account...
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(in);
                finish();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAccount();
            }
        });

        admin_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(LoginActivity.this , AdminLoginActivity.class));
             finish();
            }
        });


    }

    private void LoginAccount() {

        String password = pass_edt_log.getText().toString();
        String email = email_edt_log.getText().toString();

        if (TextUtils.isEmpty(password) || !(password.length() >=8)){
            pass_edt_log.setError(getString(R.string.Youmusthave8characters));
            pass_edt_log.requestFocus();
            return;
        }else  if (TextUtils.isEmpty(email) ||!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            email_edt_log.setError(getString(R.string.validEmail));
            email_edt_log.requestFocus();
            return;
        } else {

            dialogLoading.setTitle(getString(R.string.LoginAccount));
            dialogLoading.setMessage(getString(R.string.Credentials));
            dialogLoading.setCanceledOnTouchOutside(false);
            dialogLoading.show();

            if (checkbox_rm.isChecked()){
                Paper.book().write(Prevalent.UserEmailKey , email);
                Paper.book().write(Prevalent.UserPasswordKey , password);
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String uid = mAuth.getCurrentUser().getUid();

                        dialogLoading.dismiss();
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("user_id", uid);
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        dialogLoading.dismiss();
                        Toast.makeText(LoginActivity.this, Failedtologin, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    public void callForgetPassword(View view) {
        startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
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















