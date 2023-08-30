package com.travel.travellingbug.ui.activities.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.travel.travellingbug.R;

public class UpdateGmailActivity extends AppCompatActivity {

    private SignInClient signInClient;

    private  String RC_ONE_TAP = "001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_gmail);

        // Initialize the SignInClient
        signInClient = Identity.getSignInClient(this);

        // Build the One-Tap sign-in request
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId("YOUR_SERVER_CLIENT_ID")
                                .build())
                .build();

//        // Show the One-Tap sign-in dialog
//        signInClient.beginSignIn(signInRequest)
//                .addOnSuccessListener(this, result -> {
//                    try {
//                        Intent intentw = result.getPendingIntent().getIntentSender().sendIntent(this,0,null,null,null);
//                        Intent intent = result.getPendingIntent().getIntentSender().sendIntent(this, 0, null, null, null);
//                        startIntentSenderForResult(intent.getIntentSender(), RC_ONE_TAP, null, 0, 0, 0, null);
//                    } catch (IntentSender.SendIntentException e) {
//                        e.printStackTrace();
//                    }
//                })
//                .addOnFailureListener(this, e -> {
//                    // Handle failure
//                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_ONE_TAP) {
//            Identity.getSignInClient(this).onActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                handleSignInCredential(data);
//            }
//        }
    }

    private void handleSignInCredential(Intent data) {
//        SignInCredential credential = Identity.getSignInClient(this).getSignInCredentialFromIntent(data);
        // Use the credential to update or change the email address
        // You can use the credential.getId(), credential.getDisplayName(), credential.getEmail() methods
        // to get user information
    }
}
