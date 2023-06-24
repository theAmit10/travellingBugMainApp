package com.travel.travellingbug.ui.fragments;

import static android.content.Context.ACTIVITY_SERVICE;
import static java.lang.Thread.sleep;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.ui.activities.ChangeAddressAdtivityOne;
import com.travel.travellingbug.ui.activities.HelpActivity;
import com.travel.travellingbug.ui.activities.NotificationTab;
import com.travel.travellingbug.ui.activities.ProviderReviewActivity;
import com.travel.travellingbug.ui.activities.SplashScreen;
import com.travel.travellingbug.ui.activities.TermsAndConditionActivity;
import com.travel.travellingbug.ui.activities.WithdrawActivity;
import com.travel.travellingbug.ui.activities.login.ChangePassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.dmoral.toasty.Toasty;


public class ProfileAccountFragment extends Fragment {

    TextView changePasswordtv, postalAddresstv, helpTv, termConditionTv, ratingtv, logoutTv, notificationEmailAndSmstv, availableFundsTv;
    GoogleApiClient mGoogleApiClient;

    TextView paymentRefundsTv,dataProtectionTv,licensesTv,sendTrackingLinkTv;


    public ProfileAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_account, container, false);


        initComponenet(view);
        clickHandler();


        return view;
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //taken from google api console (Web api client id)
//                .requestIdToken("795253286119-p5b084skjnl7sll3s24ha310iotin5k4.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                FirebaseAuth.getInstance().signOut();
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {
                        if (status.isSuccess()) {
                            Log.d("MainAct", "Google User Logged out");
                           /* Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();*/
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("MAin", "Google API Client Connection Suspended");
            }
        });
    }

    public void logout() {
        Toast.makeText(getContext(), "Logging Out", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Logging Out", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Clearing All Cache ...", Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "Logging Out", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Clearing All Cache ...", Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "Cleared  All the Cache\nNow Closing App", Toast.LENGTH_LONG).show();

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(3000);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    JSONObject object = new JSONObject();
                    try {
                        object.put("id", SharedHelper.getKey(getContext(), "id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new
                            JsonObjectRequest(Request.Method.POST,
                                    URLHelper.LOGOUT,
                                    object,
                                    response -> {
                                        if (SharedHelper.getKey(getContext(), "login_by").equals("facebook"))
                                            LoginManager.getInstance().logOut();
                                        if (SharedHelper.getKey(getContext(), "login_by").equals("google"))
                                            signOut();
                                        if (!SharedHelper.getKey(getContext(), "account_kit_token").equalsIgnoreCase("")) {
                                            Log.e("MainActivity", "Account kit logout: " + SharedHelper.getKey(getContext(), "account_kit_token"));
                                            SharedHelper.putKey(getContext(), "account_kit_token", "");
                                        }


                                        //SharedHelper.putKey(context, "email", "");


                                        try {

                                            SharedHelper.clearSharedPreferences(getContext());


                                            Intent goToLogin = new Intent(getContext(), SplashScreen.class);
                                            goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(goToLogin);
                                            getActivity().finishAffinity();


                                        } catch (Exception e) {
                                            displayMessage("Error Found : " + e.getMessage());

                                        } finally {
                                            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                                                ((ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE))
                                                        .clearApplicationUserData();
                                                return;
                                            }
//
//                                            Context ctx = getApplicationContext();
//                                            PackageManager pm = ctx.getPackageManager();
//                                            Intent intent = pm.getLaunchIntentForPackage(ctx.getPackageName());
//                                            Intent mainIntent = Intent.makeRestartActivityTask(intent.getComponent());
//                                            ctx.startActivity(mainIntent);
//                                            Runtime.getRuntime().exit(0);
                                        }


                                    }, error -> {
                                displayMessage(getString(R.string.something_went_wrong));
                            }) {
                                @Override
                                public java.util.Map<String, String> getHeaders() {
                                    HashMap<String, String> headers = new HashMap<String, String>();
                                    headers.put("X-Requested-With", "XMLHttpRequest");
                                    Log.e("getHeaders: Token", SharedHelper.getKey(getContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
                                    headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getContext(), "access_token"));
                                    return headers;
                                }
                            };

                    ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

                }
            }
        });



    }


    public void displayMessage(String toastString) {
        Toasty.info(getContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void showLogoutDialog() {
        if (!getActivity().isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.logout));
            builder.setMessage(getString(R.string.exit_confirm));
            builder.setPositiveButton(R.string.yes,
                    (dialog, which) -> logout());
            builder.setNegativeButton(R.string.no, (dialog, which) -> {
                //Reset to previous seletion menu in navigation
                dialog.dismiss();
            });
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.setOnShowListener(arg -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            });
            dialog.show();
        }
    }

    private void clickHandler() {
        changePasswordtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChangePassword.class);
                startActivity(intent);
            }
        });

        postalAddresstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChangeAddressAdtivityOne.class);
                startActivity(intent);
            }
        });


        helpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        termConditionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TermsAndConditionActivity.class);
                startActivity(intent);
            }
        });

        ratingtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProviderReviewActivity.class);
                startActivity(intent);
            }
        });

        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        notificationEmailAndSmstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(() -> startActivity(new Intent(getContext(),
                        NotificationTab.class)), 250);
            }
        });

        availableFundsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(() -> startActivity(new Intent(getContext(),
                        WithdrawActivity.class)), 250);
            }
        });



        sendTrackingLinkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });

        paymentRefundsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });

        dataProtectionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });

        licensesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initComponenet(View view) {
        changePasswordtv = view.findViewById(R.id.changePasswordtv);
        postalAddresstv = view.findViewById(R.id.postalAddresstv);
        helpTv = view.findViewById(R.id.helpTv);
        termConditionTv = view.findViewById(R.id.termConditionTv);
        ratingtv = view.findViewById(R.id.ratingtv);
        logoutTv = view.findViewById(R.id.logoutTv);
        notificationEmailAndSmstv = view.findViewById(R.id.notificationEmailAndSmstv);
        availableFundsTv = view.findViewById(R.id.availableFundsTv);


        paymentRefundsTv = view.findViewById(R.id.paymentRefundsTv);
        dataProtectionTv = view.findViewById(R.id.dataProtectionTv);
        licensesTv = view.findViewById(R.id.licensesTv);
        sendTrackingLinkTv = view.findViewById(R.id.sendTrackingLinkTv);

    }
}