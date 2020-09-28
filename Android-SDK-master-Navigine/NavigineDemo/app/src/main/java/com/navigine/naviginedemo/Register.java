package com.navigine.naviginedemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mConfirmPassword,mPhone;
    Button mRegisterBtn;
    TextView mBtnGoToLogin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName=findViewById(R.id.Fullname);
    mEmail=findViewById(R.id.txtEmail);
    mPassword=findViewById(R.id.txtPassword);
    mConfirmPassword=findViewById(R.id.txtConfirmPassword);
    mPhone=findViewById(R.id.txtPhoneNumber);
    mRegisterBtn=findViewById(R.id.btnRegister);
    mBtnGoToLogin=findViewById(R.id.btnGoToLogin);

    fAuth=FirebaseAuth.getInstance();
   // progressBar=findViewById(R.id.progressBar);

    if(fAuth.getCurrentUser()!=null)
    {
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        finish();
    }



    mRegisterBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email=mEmail.getText().toString().trim();
            String password=mPassword.getText().toString().trim();
            String confirmPassword=mConfirmPassword.getText().toString().trim();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(TextUtils.isEmpty(email)){
                mEmail.setError("Email is Required");
                return;
            }
            if(TextUtils.isEmpty(password)){
                mPassword.setError("Password is Required");
                return;

            }
            if(password.length()<6){
                mPassword.setError("Password must be at least 9 Character");
                return;
            }
            if(!password.equals(confirmPassword)){
                mConfirmPassword.setError("Passwords Does not match");
                return;
            }
            if (!email.matches(emailPattern) && email.length() > 0)
            {

               mEmail.setError("Email format error please use example@mail.com");
               return;
                // or
            }
          //  progressBar.setVisibility(view.VISIBLE); caused program to crash
            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    }
                    else
                    {
                        Toast.makeText(Register.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });;
        }
    });

    mBtnGoToLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),Login.class));
        }
    });
}}