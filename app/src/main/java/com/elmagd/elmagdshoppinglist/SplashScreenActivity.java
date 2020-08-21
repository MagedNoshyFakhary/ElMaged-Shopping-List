package com.elmagd.elmagdshoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.elmagd.elmagdshoppinglist.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private FirebaseAuth mauth;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent start = new Intent(getApplicationContext(), RegistrationActivity.class);
//                startActivity(start);
//                finish();
//            }
//        }, 4000);

        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            HomeActivity.show(this);
        } else {
            LoginActivity.show(this);
        }
    }


}

  /*  private void login() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String email = sp.getString("name", "");
        String password = sp.getString("password", "");
        if (email != null && password != null) {
            mauth = FirebaseAuth.getInstance();
            mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        Toast.makeText(SplashScreenActivity.this, "Successful", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(SplashScreenActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }*/

