package com.kimadrian.kazilink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToSignUpPage(View view) {
        Intent goToSignUpPage = new Intent(this, SignUp.class);
        startActivity(goToSignUpPage);
    }

    public void goToMainPage(View view){
        Intent goToMainPage = new Intent(this, WorkLinkHome.class);
        startActivity(goToMainPage);
    }
}