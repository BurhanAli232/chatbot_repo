package com.example.burhanqurickbot;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity {

    private ArrayList<String> userList;
    private UserAdapter adapter;  // Use your custom adapter here
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize ListView
        ListView listViewUsers = findViewById(R.id.listViewUsers);
        userList = new ArrayList<>();

        // Initialize the custom adapter (UserAdapter)
        adapter = new UserAdapter(this, userList);
        listViewUsers.setAdapter(adapter);

        // Fetch and display all users
        fetchUsers();

        // Set up item click listener for user interactions if needed
        listViewUsers.setOnItemClickListener((parentView, view, position, id) -> {
            String selectedUser = userList.get(position);
            // Handle user click if needed (e.g., show more details or delete)
            Toast.makeText(ManageUsersActivity.this, "User clicked: " + selectedUser, Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear(); // Clear the previous data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();
                    String dob = snapshot.child("dob").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String firstName = snapshot.child("firstname").getValue(String.class);
                    String lastName = snapshot.child("lastname").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);

                    if (userId != null && email != null) {
                        // Create a string displaying user information
                        String userDetails = "ID: " + userId + "\n" +
                                "Email: " + email + "\n" +
                                "DOB: " + (dob != null ? dob : "N/A") + "\n" +
                                "First Name: " + (firstName != null ? firstName : "N/A") + "\n" +
                                "Last Name: " + (lastName != null ? lastName : "N/A") + "\n" +
                                "Phone: " + (phone != null ? phone : "N/A");
                        userList.add(userDetails); // Add user details to the list
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter of data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ManageUsersActivity.this, "Failed to fetch users: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
