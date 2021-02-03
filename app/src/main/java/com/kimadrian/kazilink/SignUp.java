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
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    ProgressBar myProgressBar;
    private FirebaseAuth mAuth;
    private EditText userNameEditText, emailEditText, phoneNumberEditText, professionEditText,
            passwordEditText, confirmPasswordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        myProgressBar = findViewById(R.id.myProgresssBar);
        userNameEditText = findViewById(R.id.editTextTextPersonName2);
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        phoneNumberEditText = findViewById(R.id.editTextPhone);
        professionEditText = findViewById(R.id.editTextProfession);
        passwordEditText = findViewById(R.id.editTextTextPassword2);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword3);

        mAuth = FirebaseAuth.getInstance();
    }

    public void submitButtonClicked(View view){
        String userName = userNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String profession = professionEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if(userName.isEmpty()){
            userNameEditText.setError("Username Required");
            userNameEditText.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailEditText.setError("Email Required");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Enter a valid Email address!");
            emailEditText.requestFocus();
            return;
        }
        if(phoneNumber.isEmpty()){
            phoneNumberEditText.setError("Phone Number Required");
            phoneNumberEditText.requestFocus();
            return;
        }
        if(profession.isEmpty()){
            professionEditText.setError("Profession Required");
            professionEditText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordEditText.setError("Input Password");
            passwordEditText.requestFocus();
            return;
        }
        if(confirmPassword.isEmpty()){
            confirmPasswordEditText.setError("Confirm your password");
            confirmPasswordEditText.requestFocus();
            return;
        }
        if(confirmPassword.length() < 6 || password.length() < 6){
            passwordEditText.setError("Min password is 6 Characters");
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
            passwordEditText.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
            Toast.makeText(this, "Passwords do not Match!", Toast.LENGTH_SHORT).show();
            return;
        }

        myProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(userName, email, phoneNumber, profession);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())//This returns the id for the registered user.
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUp.this,"Registration Successful : LOGIN NOW", Toast.LENGTH_LONG).show();
                                        myProgressBar.setVisibility(View.GONE);

                                        //Redirect here to the login profile.
                                        startActivity(new Intent(SignUp.this, Login.class));
                                    }
                                    else{
                                        Toast.makeText(SignUp.this, "Registration Failure", Toast.LENGTH_LONG).show();
                                        myProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(SignUp.this, "Registration Failure", Toast.LENGTH_LONG).show();
                            myProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}