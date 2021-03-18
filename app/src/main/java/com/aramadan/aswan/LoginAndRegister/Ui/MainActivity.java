package com.aramadan.aswan.LoginAndRegister.Ui;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.aramadan.aswan.Sellers.View.SellerHomeActivity;
import com.aramadan.aswan.Sellers.View.SellerRegistrationActivity;
import com.aramadan.aswan.home.Ui.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Context;

import java.text.DateFormat;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    public static final String[] languages = {"Languages:", "English", "العربية"};

    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        TextView becomePartnerTxt = findViewById(R.id.txt_become_Partner);
        Button joinUsBtn = findViewById(R.id.join_us_btn);

        ///// Language Spinner //////////
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();
                if (selectedLang.equals("العربية")) {

                    SharedPreferences arSharedPreferences = getSharedPreferences("selectedLanguage", MODE_PRIVATE);
                    SharedPreferences.Editor eneditor = arSharedPreferences.edit();
                    eneditor.putString("language", "ar");
                    eneditor.apply();
                    Toast.makeText(MainActivity.this, "من فضلك اعد فتح الابلكيشن لكى يتم دعم اللغه العربية.", Toast.LENGTH_LONG).show();

//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);

                } else if (selectedLang.equals("English")) {

                    SharedPreferences ensharedPreferences = getSharedPreferences("selectedLanguage", MODE_PRIVATE);
                    SharedPreferences.Editor ditor = ensharedPreferences.edit();
                    ditor.putString("language", "en");
                    ditor.apply();
                    Toast.makeText(MainActivity.this, "Please Restart the app to support your language.", Toast.LENGTH_LONG).show();

//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ///////////////Language Spinner/////////////////

        becomePartnerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
                   // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        joinUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (firebaseUser != null) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                   // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent in = new Intent(MainActivity.this, LoginActivity.class);
                   // in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                }
            }
        });

    }


    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("selectedLanguage", MODE_PRIVATE);
        String pine = sharedPreferences.getString("language","");
        assert pine != null;
        Locale locale = new Locale(pine);//Set Selected Locale
        Locale.setDefault(locale);//set new locale as default
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();//get Configuration
        config.setLocale(locale);//set config locale as selected locale

        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }


}




















