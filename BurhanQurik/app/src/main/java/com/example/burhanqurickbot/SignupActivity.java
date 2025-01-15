package com.example.burhanqurickbot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import android.app.DatePickerDialog;

public class SignupActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword, etPhone, etDOB;
    private Button btnSignup;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI elements
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhoneNumber);
        etDOB = findViewById(R.id.etDOB);
        btnSignup = findViewById(R.id.btnSignUp);

        // Set up the DatePicker for DOB
        etDOB.setOnClickListener(view -> showDatePickerDialog());

        // Add focus change listeners to show the keyboard when fields are focused
        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showKeyboard(etPassword);
            }
        });

        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showKeyboard(etConfirmPassword);
            }
        });

        // Signup button click listener
        btnSignup.setOnClickListener(view -> signupUser());
    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            // Display the selected date in the EditText in "dd/MM/yyyy" format
            etDOB.setText(selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void signupUser() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDOB.getText().toString().trim();

        // Check for empty fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check password length
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for valid email format
        if (!email.contains("@")) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for valid phone number format (basic check, you can expand this)
        if (phone.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Save user data to the Firebase Realtime Database
                String userId = mAuth.getCurrentUser().getUid();
                User user = new User(firstName, lastName, email, phone, dob);
                databaseReference.child(userId).setValue(user).addOnCompleteListener(databaseTask -> {
                    if (databaseTask.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish();  // Finish the signup activity to prevent the user from returning to it
                    } else {
                        Toast.makeText(SignupActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(SignupActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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