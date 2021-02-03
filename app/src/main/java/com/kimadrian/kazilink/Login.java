package com.kimadrian.kazilink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    public EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.editTextTextPersonName);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        progressBar = findViewById(R.id.progress_bar_login);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
    }

    public void goToSignUpPage(View view) {
        startActivity(new Intent(this, SignUp.class));
    }

    public void authenticateLogin(View view){

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is Required!");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Provide a Valid Email!");
            emailEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordEditText.setError("Input password!");
            passwordEditText.requestFocus();
            return;
        }
        if(password.length() < 6){
            passwordEditText.setError("Min password Length is 6");
            passwordEditText.requestFocus();
            return;
        }
        //When we reach here the user has provided legit details.
        //so now we authenticate their legitimacy from the db.
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Was successful so redirect the user to the Home Work link page.
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, WorkLinkHome.class));
                    //finish();
                }
                else{
                    Toast.makeText(Login.this, "Failed to login, Check your details", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}