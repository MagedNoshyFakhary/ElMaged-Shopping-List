package com.example.elmagdshoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText email, password;
    private Button logIn;
    private TextView singUp;
    private FirebaseAuth mauth;
    private ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_et_email);
        password = findViewById(R.id.login_et_password);
        singUp = findViewById(R.id.login_tv_singup);
        logIn = findViewById(R.id.login_btn_login);
        mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();

        mdialog = new ProgressDialog(this);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                String mPassword = password.getText().toString().trim();
                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("This field is required...");
                }
                if (TextUtils.isEmpty(mPassword)) {
                    password.setError("This field is required...");
                }
                mdialog.setMessage("Processing ...");
                mdialog.show();
                mauth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();

                        }
                    }
                });


                singUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                    }
                });
            }
        });
    }
}
