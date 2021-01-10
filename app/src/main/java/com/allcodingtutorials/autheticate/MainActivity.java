package com.allcodingtutorials.autheticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    SignInButton btnSignin;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth fbauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignin=findViewById(R.id.btnSignin);
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(getString(R.string.clientid))
        .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(this, googleSignInOptions);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=googleSignInClient.getSignInIntent();
               startActivityForResult(intent, 100);
            }
        });
        fbauth= FirebaseAuth.getInstance();
        FirebaseUser fbuser= fbauth.getCurrentUser();
            if (fbuser!=null){
                startActivity(new Intent(MainActivity.this,profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode ==100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account!=null){
                    AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    fbauth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(MainActivity.this, profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                Toast.makeText(MainActivity.this, "Sign In Succesful",Toast.LENGTH_SHORT ).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Sign In Failed"+task.getException().getMessage(),Toast.LENGTH_SHORT ).show();

                            }
                        }
                    });
                }

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}