package com.travel.travellingbug.ui.activities.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;

public class GoogleUpdateActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    CustomDialog customDialog;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }


        setContentView(R.layout.activity_google_update);



        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(mAuth.getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
//            mAuth.signOut();
        }

        customDialog = new CustomDialog(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);





        System.out.println("google started");

        getSignIn();

    }

    private void getSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {




                GoogleSignInAccount account = task.getResult(ApiException.class);
//                fireBaseAuthWithGoogle(account);


                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();


                System.out.println("google email : "+account.getEmail());
                System.out.println("google name : "+account.getDisplayName());

                SharedHelper.putKey(getApplicationContext(),"google_email",account.getEmail());
                SharedHelper.putKey(getApplicationContext(),"google_username",account.getDisplayName());
                SharedHelper.putKey(getApplicationContext(),"google_photourl", String.valueOf(account.getPhotoUrl()));


                Intent intent = new Intent();
                intent.putExtra("userName", account.getEmail());
                intent.putExtra("userMail", account.getEmail());
                intent.putExtra("userId", account.getId());
                intent.putExtra("userToken", account.getIdToken());
                setResult(Activity.RESULT_OK, intent);
                System.out.println("on google login activity");
                finish();

            } catch (ApiException e) {
                e.printStackTrace();
                onBackPressed();
            }
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount account) {
//        customDialog.show();
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = mAuth.getCurrentUser();

                        String userName = user.getDisplayName();
                        String userMail = user.getEmail();
                        String userId = user.getUid();
                        String userToken = account.getIdToken();

                        System.out.println("google email : "+userMail);
                        System.out.println("google name : "+userName);

                        SharedHelper.putKey(getApplicationContext(),"google_email",userMail);
                        SharedHelper.putKey(getApplicationContext(),"google_username",user.getDisplayName());
                        SharedHelper.putKey(getApplicationContext(),"google_photourl", String.valueOf(user.getPhotoUrl()));


                        Intent intent = new Intent();
                        intent.putExtra("userName", userName);
                        intent.putExtra("userMail", userMail);
                        intent.putExtra("userId", userId);
                        intent.putExtra("userToken", userToken);
                        setResult(Activity.RESULT_OK, intent);
                        System.out.println("on google login activity");

                        finish();
                    } else {
//                        customDialog.dismiss();
                        onBackPressed();
                    }
                });
    }
}
