package com.aramadan.aswan.home.fragmants;
/**
 * Created by:
 *    Ahmedtramadan4@gmail.com
 *    2/2021
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.aramadan.aswan.LoginAndRegister.Ui.MainActivity;
import com.aramadan.aswan.LoginAndRegister.Ui.SplashActivity;
import com.aramadan.aswan.R;

import com.google.firebase.auth.FirebaseAuth;
import java.util.Locale;
import java.util.Objects;

public class AboutFragment extends Fragment {


    private TextView txtLogOut ;
    private FirebaseAuth mAuth;
    private Spinner spinner;
    public static final String[] languages = {"Languages:", "English", "العربية"};



    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_fragmant, container, false);

        txtLogOut = view.findViewById(R.id.txt_logOut);
        spinner = view.findViewById(R.id.spinnerProfile);

        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ///// Language Spinner //////////
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();
                if (selectedLang.equals("العربية")) {

                    SharedPreferences arSharedPreferences = getContext().getSharedPreferences("selectedLanguage", Context.MODE_PRIVATE);
                    SharedPreferences.Editor eneditor = arSharedPreferences.edit();
                    eneditor.putString("language", "ar");
                    eneditor.apply();
                    startActivity(new Intent(getContext() , SplashActivity.class));

                } else if (selectedLang.equals("English")) {

                    SharedPreferences ensharedPreferences = getContext().getSharedPreferences("selectedLanguage", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ditor = ensharedPreferences.edit();
                    ditor.putString("language", "en");
                    ditor.apply();
                    startActivity(new Intent(getContext(), SplashActivity.class));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ///////////////Language Spinner/////////////////
        
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("selectedLanguage", Context.MODE_PRIVATE);
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
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    }
}