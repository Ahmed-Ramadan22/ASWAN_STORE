package com.aramadan.aswan.home.Ui;
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
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


import com.aramadan.aswan.Admin.Ui.AdminHomeActivity;
import com.aramadan.aswan.Admin.Ui.AdminLoginActivity;
import com.aramadan.aswan.Network.NetworkChangeListener;
import com.aramadan.aswan.R;
import com.aramadan.aswan.home.fragmants.CartFragment;
import com.aramadan.aswan.home.fragmants.CategoryFragment;
import com.aramadan.aswan.home.fragmants.HomeFragment;
import com.aramadan.aswan.home.fragmants.AboutFragment;
import com.aramadan.aswan.home.fragmants.SearchFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity {

    MeowBottomNavigation bottomNav;

    private final static int ID_SEARCH = 1;
    private final static int ID_CATEGORY = 2;
    private final static int ID_HOME = 3;
    private final static int ID_CART = 4;
    private final static int ID_PROFILE = 5;


    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        Paper.init(this);

        //  SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        int type = preferences.getInt(AdminLoginActivity.USER_TYPE, 2);

        bottomNav = findViewById(R.id.bottom_nev);

        bottomNav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_search));
        bottomNav.add(new MeowBottomNavigation.Model(2, R.drawable.ic_category));
        bottomNav.add(new MeowBottomNavigation.Model(3, R.drawable.ic_home));
        bottomNav.add(new MeowBottomNavigation.Model(4, R.drawable.ic_shopping_cart));
        bottomNav.add(new MeowBottomNavigation.Model(5, R.drawable.ic_about_us));


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.root_container, new HomeFragment());
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
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                int type = preferences.getInt(AdminLoginActivity.USER_TYPE, 1);

                Fragment fragmentSelectedId = null;
                switch (item.getId()) {

                    case ID_SEARCH:
                        fragmentSelectedId = new SearchFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.root_container, fragmentSelectedId).commit();
                        break;

                    case ID_CATEGORY:
                        fragmentSelectedId = new CategoryFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.root_container, fragmentSelectedId).commit();
                        break;
                    case ID_HOME:
                        fragmentSelectedId = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.root_container, fragmentSelectedId).commit();
                        break;
                    case ID_CART:

                            fragmentSelectedId = new CartFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.root_container, fragmentSelectedId).commit();
                        break;
                    case ID_PROFILE:

                            fragmentSelectedId = new AboutFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.root_container, fragmentSelectedId).commit();

                        break;
                }

            }
        });
        bottomNav.show(ID_HOME, true);

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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CONSOLE", "ASWAN onStart");

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }


}