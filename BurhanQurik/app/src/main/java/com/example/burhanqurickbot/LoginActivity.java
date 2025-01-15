package com.example.burhanqurickbot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/** @noinspection ALL*/
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Link UI elements
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnlogin);
        Button btnGoToSignup = findViewById(R.id.btnGoToSignup);

        // Login button logic
        btnLogin.setOnClickListener(view -> loginUser());

        // Navigate to SignupActivity
        btnGoToSignup.setOnClickListener(view -> {
            Log.d("LoginActivity", "Go to Signup button clicked");
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d("LoginActivity", "Attempting login with email: " + email);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("LoginActivity", "Login successful.");

                if (mAuth.getCurrentUser() != null) {
                    String userId = mAuth.getCurrentUser().getUid();
                    Log.d("LoginActivity", "Current user ID: " + userId);

                    // Check if user exists in the database
                    databaseReference.child(userId).get().addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful() && dbTask.getResult().exists()) {
                            Log.d("LoginActivity", "User data exists in the database.");
                            databaseReference.child(userId).child("lastLogin").setValue(System.currentTimeMillis()).addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    Log.d("LoginActivity", "Last login time updated successfully.");
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, ChatActivity.class));
                                    finish(); // Close the login activity
                                } else {
                                    Log.e("LoginActivity", "Failed to update last login: " + Objects.requireNonNull(updateTask.getException()).getMessage());
                                    Toast.makeText(LoginActivity.this, "Failed to update last login", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Log.e("LoginActivity", "User data does not exist or database read failed.");
                            Toast.makeText(LoginActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("LoginActivity", "User ID is null after login.");
                    Toast.makeText(LoginActivity.this, "User ID is null after login.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("LoginActivity", "Login failed: " + Objects.requireNonNull(task.getException()).getMessage());
                Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Open Facebook profile
    // Open Facebook profile
    public void openFacebookProfile(View view) {
        String facebookProfileUrl = "https://www.facebook.com/burhanali4545"; // Correct link

        // Try to open with createChooser for a selection of available apps
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookProfileUrl));
        Intent chooser = Intent.createChooser(intent, "Open with");

        // Check if there's an app to handle the intent
        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        } else {
            // Open in the browser if no app can handle it
            Toast.makeText(this, "No app can open the Facebook profile", Toast.LENGTH_SHORT).show();
        }
    }

    // Open LinkedIn profile
    public void openLinkedInProfile(View view) {
        String linkedInProfileUrl = "https://www.linkedin.com/in/burhanali4545"; // Correct link

        // Try to open with createChooser for a selection of available apps
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkedInProfileUrl));
        Intent chooser = Intent.createChooser(intent, "Open with");

        // Check if there's an app to handle the intent
        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        } else {
            // Open in the browser if no app can handle it
            Toast.makeText(this, "No app can open the LinkedIn profile", Toast.LENGTH_SHORT).show();
        }
    }

    // Open Twitter profile
    public void openTwitterProfile(View view) {
        String twitterProfileUrl = "https://twitter.com/BurhanAli2322"; // Correct link

        // Try to open with createChooser for a selection of available apps
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterProfileUrl));
        Intent chooser = Intent.createChooser(intent, "Open with");

        // Check if there's an app to handle the intent
        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        } else {
            // Open in the browser if no app can handle it
            Toast.makeText(this, "No app can open the Twitter profile", Toast.LENGTH_SHORT).show();
        }
    }
}