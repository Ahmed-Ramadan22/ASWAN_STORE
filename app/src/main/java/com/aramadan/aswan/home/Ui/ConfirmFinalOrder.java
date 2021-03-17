package com.aramadan.aswan.home.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrder extends AppCompatActivity {


    private EditText shipmentName, shipmentPhoneNumber, shipmentHomeAddress, shipmentOrdersDetails;
    private Button confirmFinalOrder;
    String totalAmount = "";

    private FirebaseAuth mAuth;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        mAuth = FirebaseAuth.getInstance();

        //Initialization
        shipmentName = findViewById(R.id.shipment_name);
        shipmentPhoneNumber = findViewById(R.id.shipment_phone_number);
        shipmentHomeAddress = findViewById(R.id.shipment_home_address);
        confirmFinalOrder = findViewById(R.id.confirm_final_order);
        shipmentOrdersDetails = findViewById(R.id.shipment_orders_Des);
        confirmFinalOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOrder();
            }
        });


    }

    private void CheckOrder() {

        if (TextUtils.isEmpty(shipmentName.getText().toString())) {
            shipmentName.setError("Please Provide Your full Name.");
        } else if (TextUtils.isEmpty(shipmentPhoneNumber.getText().toString())) {
            shipmentPhoneNumber.setError("Please Provide Your Phone Number.");
        } else if (TextUtils.isEmpty(shipmentHomeAddress.getText().toString())) {
            shipmentHomeAddress.setError("Please Provide Your Home Address.");
        } else if (TextUtils.isEmpty(shipmentOrdersDetails.getText().toString())) {
            shipmentOrdersDetails.setError("Please Provide Your Details.");
        }else {
            ConfirmOrder();
        }

    }

    private void ConfirmOrder() {

        final String saveCurrentDate, saveCurrentTime;

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String userN = mAuth.getCurrentUser().getUid();

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(userN);

        final HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", shipmentName.getText().toString());
        ordersMap.put("phone", shipmentPhoneNumber.getText().toString());
        ordersMap.put("address", shipmentHomeAddress.getText().toString());
        ordersMap.put("detailsOrder", shipmentOrdersDetails.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");

        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(userN)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ConfirmFinalOrder.this, "Your final order has been placed successfully.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrder.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            });
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


















