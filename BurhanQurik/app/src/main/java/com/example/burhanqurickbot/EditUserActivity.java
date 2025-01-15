package com.example.burhanqurickbot;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPhone, etDob;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Get the user ID passed from the previous activity
        userId = getIntent().getStringExtra("USER_ID");

        // Initialize EditText fields
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etDob = findViewById(R.id.etDOB);

        // Fetch the current user data and fill the fields (for editing)
        fetchUserData();

        // Handle Save button click to update user details
        findViewById(R.id.btnSave).setOnClickListener(view -> saveUserData());
    }

    private void fetchUserData() {
        // Fetch data using the userId
        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                // Populate the EditText fields with the current user data
                String firstName = task.getResult().child("firstName").getValue(String.class);
                String lastName = task.getResult().child("lastName").getValue(String.class);
                String email = task.getResult().child("email").getValue(String.class);
                String phone = task.getResult().child("phone").getValue(String.class);
                String dob = task.getResult().child("dob").getValue(String.class);

                etFirstName.setText(firstName);
                etLastName.setText(lastName);
                etEmail.setText(email);
                etPhone.setText(phone);
                etDob.setText(dob);
            } else {
                Toast.makeText(EditUserActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData() {
        // Get updated data from the EditText fields
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();

        // Validate fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty()) {
            Toast.makeText(EditUserActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the user data in Firebase
        User updatedUser = new User(firstName, lastName, email, phone, dob);
        databaseReference.child(userId).setValue(updatedUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditUserActivity.this, "User details updated successfully.", Toast.LENGTH_SHORT).show();
                finish(); // Go back to the previous activity
            } else {
                Toast.makeText(EditUserActivity.this, "Failed to update user.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
