package com.simplexo.alaamchannel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email ,password ;
    private Button btn_auth ;
    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener  mAuthListener;

    private InterstitialAd interstitialAd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        declare();
        MobileAds.initialize(AuthActivity.this,"ca-app-pub-9502802921397120~4573557064");
        AdRequest adRequest = new AdRequest.Builder().build();
        // Prepare the Interstitial Ad
        interstitialAd = new InterstitialAd(AuthActivity.this);
// Insert the Ad Unit ID
        interstitialAd.setAdUnitId("ca-app-pub-9502802921397120/8838754062");
        interstitialAd.loadAd(adRequest);
// Prepare an Interstitial Ad Listener
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
// Call displayInterstitial() function
                displayInterstitial();
            }
        });
    }
    private void declare() {
        email = (EditText) findViewById(R.id.editText_email_AuthAcivity);
        password = (EditText) findViewById(R.id.editText_password_AuthActivity);
        btn_auth = (Button) findViewById(R.id.btn_AuthActivity);
        btn_auth.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(AuthActivity.this, ChooseActioActivity.class));
                }
            }
        };
    }
    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
    @Override
    public void onClick(View v) {
        if(v==btn_auth)
        {
            signIn();
        }
    }
    private void signIn() {
        String emailString = email.getText().toString().trim();
        String passwordString =  password.getText().toString().trim();
        if(! TextUtils.isEmpty(emailString)|| ! TextUtils.isEmpty(passwordString))
        {
            mAuth.signInWithEmailAndPassword(emailString , passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        startActivity(new Intent(AuthActivity.this , ChooseActioActivity.class));
                    }else
                    {
                        Toast.makeText(AuthActivity.this, "sign in filed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(this,"please type email and password", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
