package com.travel.travellingbug.ui.activities.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;

import java.util.Arrays;

public class FaceBookLoginActivity extends AppCompatActivity {

//    https://travellingbug-cb35b.firebaseapp.com/__/auth/handler

    String TAG = "FBAUTH";

    CustomDialog customDialog;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    FacebookSdk facebookSdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_face_book_login);
        customDialog = new CustomDialog(this);
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
//        FacebookSdk.setClientToken(FacebookSdk.getClientToken());
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFaceBookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                onBackPressed();
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                onBackPressed();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    private void handleFaceBookAccessToken(AccessToken accessToken) {

//        customDialog.show();
        if( !isFinishing()) {
            customDialog.show();
        }



//        if(!isFinishing() && customDialog != null) { customDialog.show();}
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userName = user.getDisplayName();
                        String userMail = user.getEmail();
                        String userId = user.getUid();
                        String userToken = accessToken.getToken();

//                        Toast.makeText(this, "name : "+userName, Toast.LENGTH_SHORT).show();
                        System.out.println("name : "+userName);
                        System.out.println("name : "+userMail);
                        System.out.println("name : "+user.getPhotoUrl());

                        SharedHelper.putKey(getApplicationContext(),"google_email",userMail);
                        SharedHelper.putKey(getApplicationContext(),"google_username",user.getDisplayName());
                        SharedHelper.putKey(getApplicationContext(),"google_photourl", String.valueOf(user.getPhotoUrl()));


                        Intent intent = new Intent();
                        intent.putExtra("userName", userName);
                        intent.putExtra("userMail", userMail);
                        intent.putExtra("userId", userId);
                        intent.putExtra("userToken", userToken);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(FaceBookLoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
