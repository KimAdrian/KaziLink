package com.kimadrian.kazilink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goToLoginPage(View view) {
        Intent goToLoginActivity = new Intent(this, Login.class);
        startActivity(goToLoginActivity);
        finish();
    }
}