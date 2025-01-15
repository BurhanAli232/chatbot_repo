package com.example.burhanqurickbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Make sure your layout XML file is named activity_main.xml

        // Initialize UI elements
        getStartedButton = findViewById(R.id.get_started_button);

        // Set an OnClickListener to the Get Started button
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the SignupActivity or LoginActivity
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);  // Change to LoginActivity if needed
                startActivity(intent);
                finish();  // Close the current MainActivity
            }
        });
    }
}
