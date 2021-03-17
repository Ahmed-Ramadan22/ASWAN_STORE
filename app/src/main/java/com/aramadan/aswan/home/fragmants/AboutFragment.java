package com.aramadan.aswan.home.fragmants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.text.TextUtils;
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

import com.aramadan.aswan.LoginAndRegister.Models.Users;
import com.aramadan.aswan.LoginAndRegister.Prevalent.Prevalent;
import com.aramadan.aswan.LoginAndRegister.Ui.MainActivity;
import com.aramadan.aswan.R;
import com.aramadan.aswan.home.Model.Products;
import com.aramadan.aswan.home.Ui.HomeActivity;
import com.aramadan.aswan.home.Ui.ProductDetailsActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


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
                    Toast.makeText(getContext(),  R.string.language, Toast.LENGTH_LONG).show();

//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);

                } else if (selectedLang.equals("English")) {

                    SharedPreferences ensharedPreferences = getContext().getSharedPreferences("selectedLanguage", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ditor = ensharedPreferences.edit();
                    ditor.putString("language", "en");
                    ditor.apply();
                    Toast.makeText(getContext(), R.string.language, Toast.LENGTH_LONG).show();

//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
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