package com.example.burhanqurickbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private EditText etAdminId;
    private Button btnVerify;
    private static final String ADMIN_USER_ID = "03466675232"; // Replace with your actual admin ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Link the UI elements
        etAdminId = findViewById(R.id.etAdminId);
        btnVerify = findViewById(R.id.btnVerify);

        // Check if the EditText and Button are not null
        if (etAdminId == null || btnVerify == null) {
            Toast.makeText(this, "UI elements not properly initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set up the button listener for verifying admin ID
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredId = etAdminId.getText().toString().trim();
                if (enteredId.isEmpty()) {
                    Toast.makeText(AdminActivity.this, "Please enter Admin ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (enteredId.equals(ADMIN_USER_ID)) {
                    // If the entered ID matches, navigate to AdminDashboardActivity
                    Intent intent = new Intent(AdminActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                    finish(); // Close AdminActivity so the user can't go back to it
                } else {
                    // Show an error message if the ID is incorrect
                    Toast.makeText(AdminActivity.this, "Invalid Admin ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
