package com.kimadrian.kazilink;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class SignUp extends AppCompatActivity {

    ProgressBar myProgressBar;
    private FirebaseAuth mAuth;
    private EditText userNameEditText, emailEditText, phoneNumberEditText, professionEditText,
            passwordEditText, confirmPasswordEditText, userDescriptionEditText;
    private Uri imageUri;
    public FirebaseStorage storage;
    public StorageReference storageReference;
    public DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    public boolean hasUserUploadedPicture;
    private String profilePicImageUrl;
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
        userDescriptionEditText = findViewById(R.id.editTextDescription);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void submitButtonClicked(View view){
        String userName = userNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String profession = professionEditText.getText().toString();
        String userDescription = userDescriptionEditText.getText().toString();
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
        if(userDescription.isEmpty()){
            userDescriptionEditText.setError("A Brief description is Required.");
            userDescriptionEditText.requestFocus();
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
        if(!hasUserUploadedPicture){
            Toast.makeText(this, "Please Upload a Profile Picture.", Toast.LENGTH_SHORT).show();
            return;
        }

        myProgressBar.setVisibility(View.VISIBLE);
        //This will get the currentuser ID.
        //String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //String imageUrl = imageUri.toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(userName, email, phoneNumber, profession, userDescription, profilePicImageUrl );

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
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(SignUp.this, "Registration Failure : Check Connection", Toast.LENGTH_LONG).show();
                                        myProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(SignUp.this, "Registration Failure : Check Connection", Toast.LENGTH_LONG).show();
                            myProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void uploadProfilePicture(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            uploadPictureToFirebase();
        }
        else{
            Toast.makeText(getApplicationContext(),"Select an Image.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPictureToFirebase(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();
        final String randomKey = UUID.randomUUID().toString();

        StorageReference myStorageReference  = storageReference.child("images" + randomKey);
        myStorageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        hasUserUploadedPicture = true;
                        myStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                profilePicImageUrl = uri.toString();
                                /*User user = new User(uri.toString());
                                String userId = root.push().getKey();
                                root.child(userId).setValue(user);*/
                                Snackbar.make(findViewById(android.R.id.content), "Profile Picture Uploaded" , Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Unable to Upload Picture", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercentage = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Progress : " + (int)progressPercentage + "%");
                    }
                });
    }
}