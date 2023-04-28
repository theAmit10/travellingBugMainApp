package com.pinkcar.providers.ui.activities.login;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pinkcar.providers.R;
import com.pinkcar.providers.helper.SharedHelper;
import com.pinkcar.providers.ui.activities.HomeScreenActivity;
import com.pinkcar.providers.ui.activities.IntroScreen;
import com.pinkcar.providers.ui.activities.SplashScreen;

import java.util.Locale;

public class IntroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    RadioGroup radioGroup;
    Button btnSubmit;
    boolean localeHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_intro);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        btnSubmit.setOnClickListener(v -> {
            if (SharedHelper.getKey(IntroActivity.this, "selectedlanguage") != null) {
//                Intent refresh = new Intent(IntroActivity.this, SplashScreen.class);
                Intent refresh = new Intent(IntroActivity.this, IntroScreen.class);
                startActivity(refresh);
                finish();
            } else {
                Toast.makeText(IntroActivity.this, getString(R.string.choose_language), Toast.LENGTH_SHORT).show();
            }
        });
        setLanguage();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(IntroActivity.this, HomeScreenActivity.class);
            startActivity(intent);
        }
    }

    private void setLanguage() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdEng) {
                SharedHelper.putKey(IntroActivity.this, "selectedlanguage", "en");
                setLocale("en");
            }
            if (checkedId == R.id.rdArb) {
                SharedHelper.putKey(IntroActivity.this, "selectedlanguage", "ar");
                setLocale("ar");
            }
        });
    }


    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        localeHasChanged = true;
        btnSubmit.setText(getString(R.string.submit));
    }

    public void onResume() {
        super.onResume();
    }
}
