package com.example.burhanqurickbot;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewReportActivity extends AppCompatActivity {

    private EditText etEmail;
    private ListView listViewResults;
    private ArrayList<String> searchResults;
    private ArrayAdapter<String> adapter;
    private DatabaseReference databaseReference;
    private String selectedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewreport);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        listViewResults = findViewById(R.id.listViewResults);
        searchResults = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.tvListItem, searchResults);
        listViewResults.setAdapter(adapter);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Handle search button click
        findViewById(R.id.btnSearch).setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            if (isValidEmail(email)) {
                searchUserByEmail(email.toLowerCase()); // Search users by email
            } else {
                Toast.makeText(ViewReportActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up ListView item click listener
        listViewResults.setOnItemClickListener((parentView, view, position, id) -> {
            String selectedUser = searchResults.get(position);
            selectedUserId = selectedUser.split("\n")[0].split(":")[1].trim(); // Extract the user ID from the details
            showUserOptions();
        });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showUserOptions() {
        // Create a TextView to set as the title
        TextView title = new TextView(ViewReportActivity.this);
        title.setText("Admin Options");
        title.setTextSize(20);  // Adjust size as needed
        title.setTypeface(null, Typeface.BOLD);  // Set text to bold
        title.setTextColor(getResources().getColor(R.color.facebook));  // You can set a color as well if needed
        title.setPadding(10, 10, 10, 10);  // Optional: add padding to the title

        // Create the AlertDialog.Builder and set the custom title
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewReportActivity.this, R.style.CustomAlertDialogTheme);
        builder.setCustomTitle(title);  // Set the custom TextView as the title

        // Set the dialog items (Edit, Delete)
        builder.setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    editUser();
                    break;
                case 1:
                    confirmDelete();
                    break;
            }
        });

        // Show the dialog
        builder.create().show();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this, R.style.CustomAlertDialogTheme)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", (dialog, which) -> deleteUser())
                .setNegativeButton("No", null)
                .show();
    }

    private void searchUserByEmail(String email) {
        searchResults.clear();
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey();
                        String dob = snapshot.child("dob").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String firstName = snapshot.child("firstName").getValue(String.class);
                        String lastName = snapshot.child("lastName").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);

                        String userDetails = "ID: " + userId + "\n" +
                                "Email: " + email + "\n" +
                                "DOB: " + (dob != null ? dob : "N/A") + "\n" +
                                "First Name: " + (firstName != null ? firstName : "N/A") + "\n" +
                                "Last Name: " + (lastName != null ? lastName : "N/A") + "\n" +
                                "Phone: " + (phone != null ? phone : "N/A");

                        searchResults.add(userDetails);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ViewReportActivity.this, "No user found with this email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewReportActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUser() {
        if (selectedUserId != null) {
            databaseReference.child(selectedUserId).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ViewReportActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                            searchResults.clear();
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ViewReportActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void editUser() {
        if (selectedUserId != null) {
            Intent intent = new Intent(ViewReportActivity.this, EditUserActivity.class);
            intent.putExtra("USER_ID", selectedUserId);
            startActivity(intent);
        }
    }
}
