package com.example.burhanqurickbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnManageUsers, btnViewReports, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Link the UI elements
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnViewReports = findViewById(R.id.btnViewReports);
        btnLogout = findViewById(R.id.btnLogout);

        // Set up button listeners
        btnManageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboardActivity.this, ManageUsersActivity.class);
                startActivity(intent);
            }
        });

        btnViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a Toast message
                Toast.makeText(AdminDashboardActivity.this, "Opening Reports...", Toast.LENGTH_SHORT).show();

                // Create an Intent to start the Viewreport activity
                Intent intent = new Intent(AdminDashboardActivity.this, ViewReportActivity.class);

                // Start the Viewreport activity
                startActivity(intent);

                // Add functionality for viewing reports here (if needed)
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log out and return to the login screen
                Toast.makeText(AdminDashboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close AdminDashboardActivity
            }
        });
    }
}
