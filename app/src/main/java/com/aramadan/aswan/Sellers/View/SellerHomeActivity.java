package com.aramadan.aswan.Sellers.View;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.aramadan.aswan.LoginAndRegister.Ui.MainActivity;
import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.aramadan.aswan.Sellers.SellerFragment.SellerCategoryFragment;
import com.aramadan.aswan.Sellers.SellerFragment.HomeSellerFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;

public class SellerHomeActivity extends AppCompatActivity {

    private final static int ID_HOME = 1;
    private final static int ID_ADD = 2;
    private final static int ID_LOGOUT = 3;
    private MeowBottomNavigation bottomNav;
    private FirebaseAuth mAuth;

    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        mAuth = FirebaseAuth.getInstance();
        bottomNav = findViewById(R.id.bottom_nev_seller);

        bottomNav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNav.add(new MeowBottomNavigation.Model(2, R.drawable.ic_category));
        bottomNav.add(new MeowBottomNavigation.Model(3, R.drawable.ic_logout));

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.root_container_Seller, new HomeSellerFragment());
        fragmentTransaction.commit();

        bottomNav.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNav.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNav.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                Fragment fragmentSelectedId = null;
                switch (item.getId()) {
                    case ID_HOME:
                        fragmentSelectedId = new HomeSellerFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.root_container_Seller, fragmentSelectedId).commit();

                        break;

                    case ID_ADD:
                        fragmentSelectedId = new SellerCategoryFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.root_container_Seller, fragmentSelectedId).commit();
                        break;

                    case ID_LOGOUT:

                        mAuth.signOut();
                        Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                }
            }
        });
        bottomNav.show(ID_HOME, true);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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