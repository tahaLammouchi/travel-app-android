package com.example.travel_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

       public class AddTravel extends AppCompatActivity {

        private EditText titleEditText;
        private EditText descriptionEditText;
        private Button selectEmplacementButton, createButton;
        ImageView backButton, travelPicture;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_travel_activity);

            // Find views by ID
            titleEditText = findViewById(R.id.add_travel_title);
            descriptionEditText = findViewById(R.id.add_travel_descrption);
            selectEmplacementButton = findViewById(R.id.add_travel_select_emplacement);
            createButton = findViewById(R.id.add_travel_create);
            backButton = findViewById(R.id.add_travel_back);
            travelPicture = findViewById(R.id.add_travel_picture);


            // Set click listeners
            selectEmplacementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle SELECT EMPLACEMENT button click
                    onSelectEmplacementButtonClick();
                }
            });

            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle CREATE button click
                    onCreateButtonClick();
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle back button click (if needed)
                    onBackPressed();
                }
            });
        }

        private void onSelectEmplacementButtonClick() {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            Intent i = new Intent(AddTravel.this, Maps.class);
            i.putExtra("title", title);
            i.putExtra("description", description);
            startActivity(i);
        }

        private void onCreateButtonClick() {
            // Retrieve values from EditText fields
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            // Add validation or further processing as needed
            if (!title.isEmpty() && !description.isEmpty()) {
                // Add logic for handling CREATE button click
                // You can use the retrieved values (title, description) as needed
                Toast.makeText(this, "CREATE button clicked\nTitle: " + title + "\nDescription: " + description, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();
            }
        }


       }
