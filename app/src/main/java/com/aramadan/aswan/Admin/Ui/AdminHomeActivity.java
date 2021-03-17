package com.aramadan.aswan.Admin.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;

import android.view.View;
import com.aramadan.aswan.LoginAndRegister.Ui.MainActivity;
import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Ui.HomeActivity;
public class AdminHomeActivity extends AppCompatActivity {

    private Button logoutBtn, checkOrderBtn, maintainProductsBtn , checkApproveProductBtn;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logoutBtn = findViewById(R.id.logout_btn);
        checkOrderBtn = findViewById(R.id.check_orders_btn);
        maintainProductsBtn = findViewById(R.id.maintain_btn);
        checkApproveProductBtn = findViewById(R.id.check_approve_product_btn);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AdminHomeActivity.this, MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                finish();
            }
        });

        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(inte);
            }
        });

        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminEditDeleteProductActivity.class);
                intent.putExtra("Admins", "Admins");
                startActivity(intent);
            }
        });

        checkApproveProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminHomeActivity.this, AdminCheckNewProductActivity.class);
                startActivity(i);
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