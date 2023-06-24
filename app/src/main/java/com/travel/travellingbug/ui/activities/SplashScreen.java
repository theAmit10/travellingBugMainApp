package com.travel.travellingbug.ui.activities;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.ui.activities.login.IntroActivity;
import com.travel.travellingbug.ui.activities.login.SignUp;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final String TAG = SplashScreen.class.getSimpleName();
    ConnectionHelper helper;
    Boolean isInternet;
    Handler handleCheckStatus;

    String device_token, device_UDID;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    String keys = "ojBHda1ppwq9Fdc8lTJ507dNQkfBWAG1" + ":" + "Ixy9QVRAnoDrmT1I";

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            String packageName = context.getApplicationContext().getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();


        // To Remove Status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    if (SharedHelper.getKey(SplashScreen.this, "selectedlanguage").contains("ar")) {
                        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    } else {
                        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    }
                    ButterKnife.bind(SplashScreen.this);

                    printKeyHash(SplashScreen.this);

                    GetToken();

                    helper = new ConnectionHelper(SplashScreen.this);
                    isInternet = helper.isConnectingToInternet();
                    String base64Key = Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP);



                    SplashScreen.this.runOnUiThread(new Runnable() {
                        public void run() {
                            handleCheckStatus = new Handler();
                        }
                    });

                    //check status every 3 sec
                    SharedHelper.putKey(SplashScreen.this, "base64Key", base64Key);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    showPermissionDialog();

//                    helper = new ConnectionHelper(SplashScreen.this);
//                    isInternet = helper.isConnectingToInternet();
//                    new Thread(() -> {
//                        while (progressStatus < 101) {
//                            progressStatus += 25;
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            handler.post(() -> progressBar.setProgress(progressStatus));
//                        }
//                    }).start();

                    Log.e("printKeyHash", printKeyHash(SplashScreen.this) + "");

//                        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
//                            @Override
//                            public void onComplete(@NonNull @NotNull Task<InstallationTokenResult> task) {
////                String newToken = task.getResult().getToken();
//                                String newToken = "addedFakeToken";
//                                Log.e("newToken", newToken);
//                                SharedHelper.putKey(getApplicationContext(), "device_token", "" + newToken);
//                                device_token = newToken;
//                            }
//                        });

//                    FirebaseInstallations.getInstance().getToken(/* forceRefresh */true)
//                            .addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<InstallationTokenResult> task) {
//                                    if (task.isSuccessful() && task.getResult() != null) {
//                                        Log.d("Installations", "Installation auth token: " + task.getResult().getToken());
//                                        String newToken = task.getResult().getToken();
//                                        Log.e("newToken", newToken);
//                                        SharedHelper.putKey(getApplicationContext(), "device_token", "" + newToken);
//                                        device_token = newToken;
//                                    } else {
//                                        Log.e("Installations", "Unable to get Installation auth token");
//                                    }
//                                }
//                            });

//                    }
//                    else {
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("FirstTimeInstall","Yes");
//                        editor.apply();
//
//                        startActivity(new Intent(SplashScreen.this, IntroScreen.class));
//


                }
            }
        });





    }



    @SuppressLint("HardwareIds")
    public void GetToken() {
        try {
            if (!SharedHelper.getKey(this, "device_token").equals("") &&
                    SharedHelper.getKey(this, "device_token") != null) {
                device_token = SharedHelper.getKey(this, "device_token");
                Log.i(TAG, "GCM Registration Token: " + device_token);
                Log.i(TAG, "device_token: " + device_token);
            } else {
//                FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<InstallationTokenResult> task) {
//                        String newToken = task.getResult().getToken();
////                        String newToken = "NoTokenTypeDataAdded";
//                        Log.e("newToken", newToken);
//                        SharedHelper.putKey(getApplicationContext(), "device_token", "" + newToken);
//                        device_token = newToken;
//                    }
//                });

                FirebaseInstallations.getInstance().getToken(/* forceRefresh */true)
                        .addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    String newToken = task.getResult().getToken();
                                    Log.d("Installations", "Installation auth token: " + task.getResult().getToken());
                                    Log.e("newToken", newToken);
                                    SharedHelper.putKey(getApplicationContext(), "device_token", "" + newToken);
                                    device_token = newToken;
                                } else {
                                    Log.e("Installations", "Unable to get Installation auth token");
                                }
                            }
                        });
            }
        } catch (Exception e) {
            device_token = "COULD NOT GET FCM TOKEN";
            Log.d(TAG, "Failed to complete token refresh", e);
        }


        try {
            device_UDID = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            SharedHelper.putKey(getApplicationContext(),"device_udid",device_UDID);

            Log.i(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }

    @SuppressLint("HardwareIds")
    public void getProfile() {
        String device_UDID = "";

        try {
            device_UDID = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.i(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
        String url = URLHelper.USER_PROFILE_API + "?device_type=android&device_id="
                + device_UDID + "&device_token=" + device_token;
        Log.v("profileur", url);
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        url,
                        object,
                        response -> {
                            Log.e("responseBody", response.toString());
                            SharedHelper.putKey(this, "id", response.optString("id"));
                            SharedHelper.putKey(this, "first_name", response.optString("first_name"));
                            SharedHelper.putKey(this, "email", response.optString("email"));
                            SharedHelper.putKey(this, "sos", response.optString("sos"));
                            SharedHelper.putKey(this, "currency", response.optString("currency"));
                            SharedHelper.putKey(this, "rating", response.optString("rating"));
                            if (response.optString("avatar").startsWith("http"))
                                SharedHelper.putKey(this, "picture", response.optString("avatar"));
                            else
                                SharedHelper.putKey(this, "picture", URLHelper.BASE + "storage/app/public/" + response.optString("avatar"));
                            SharedHelper.putKey(this, "gender", response.optString("gender"));
                            SharedHelper.putKey(this, "mobile", response.optString("mobile"));
                            SharedHelper.putKey(this, "approval_status", response.optString("status"));
                            SharedHelper.putKey(this, "loggedIn", getString(R.string.True));
                            if (response.optJSONObject("service") != null) {
                                try {
                                    JSONObject service = response.optJSONObject("service");
                                    if (service.optJSONObject("service_type") != null) {
                                        JSONObject serviceType = service.optJSONObject("service_type");
                                        SharedHelper.putKey(this, "service",
                                                serviceType.optString("name"));
                                        SharedHelper.putKey(this, "service_image",
                                                serviceType.optString("image"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (response.optString("status").equalsIgnoreCase("new")) {
                                Intent intent = new Intent(this, WaitingForApproval.class);
                                startActivity(intent);
                                finish();
                            } else {
                                GoToMainActivity();

                            }

                        },
                        error -> {
                            SharedHelper.clearSharedPreferences(SplashScreen.this);
                            displayMessage(getString(R.string.something_went_wrong));
                            GoToBeginActivity();
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " +
                                SharedHelper.getKey(SplashScreen.this, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void GoToMainActivity() {
        Intent mainIntent = new Intent(this, HomeScreenActivity.class);
//        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(this, "loggedIn", getString(R.string.False));
//        Intent mainIntent = new Intent(this, Login.class);
        Intent mainIntent = new Intent(this, SignUp.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void showPermissionDialog() {
        Dexter.withActivity(SplashScreen.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (SharedHelper.getKey(getApplicationContext(), "selectedlanguage") != null &&
                                    !SharedHelper.getKey(getApplicationContext(), "selectedlanguage").isEmpty()) {
                                setLocale(SharedHelper.getKey(getApplicationContext(), "selectedlanguage"));
                                handleCheckStatus.postDelayed(() -> {
                                    if (SharedHelper.getKey(SplashScreen.this, "loggedIn").equalsIgnoreCase(getString(R.string.True))) {
                                        System.out.println("WASU SharedHelper.getKey(SplashScreen.this, \"loggedIn\").equalsIgnoreCase(getString(R.string.True)) : " + SharedHelper.getKey(SplashScreen.this, "loggedIn").equalsIgnoreCase(getString(R.string.True)));
//                                        getProfile();
                                        GoToMainActivity();

                                    } else {
                                        System.out.println("WASU not SharedHelper.getKey(SplashScreen.this, \"loggedIn\").equalsIgnoreCase(getString(R.string.True)) : " + SharedHelper.getKey(SplashScreen.this, "loggedIn").equalsIgnoreCase(getString(R.string.True)));
                                        GoToBeginActivity();
                                    }
                                }, 3000);
                            } else {
                                System.out.println("WASU else from splash screen executed.");
                                startActivity(new Intent(SplashScreen.this, IntroActivity.class));
                                finish();
                            }
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    boolean localeHasChanged = false;

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        localeHasChanged = true;
    }

}
