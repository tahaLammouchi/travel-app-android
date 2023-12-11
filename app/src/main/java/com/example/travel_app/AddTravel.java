package com.example.travel_app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_app.entities.Travel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddTravel extends AppCompatActivity {

        private EditText titleEditText;
        private EditText descriptionEditText;
        private ImageView backButton, travelPicture;

    private Button selectEmplacementButton, createButton;
    private Double latitude, longitude;
    private String country="",title,description;
    private String imageURL;
    private Uri uri;

    /*FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(mAuth != null)
            if(mAuth.getCurrentUser() != null)
                currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            Intent intent = new Intent(AddTravel.this, Login.class);
            startActivity(intent);
            finish();
        }
    }*/

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

            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK){
                                Intent data = result.getData();
                                uri = data.getData();
                                travelPicture.setImageURI(uri);
                            } else {
                                Toast.makeText(AddTravel.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
            // Set click listeners

            travelPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent photoPicker = new Intent(Intent.ACTION_PICK);
                    photoPicker.setType("image/*");
                    activityResultLauncher.launch(photoPicker);
                }
            });

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
                    Intent intent = new Intent(AddTravel.this, Home.class);
                    startActivity(intent);
                }
            });
        }

        private void onSelectEmplacementButtonClick() {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            Intent i = new Intent(AddTravel.this, Maps.class);
            i.putExtra("title", title);
            i.putExtra("description", description);
            startActivityForResult(i, 0000);
        }

        private void onCreateButtonClick() {
            // Retrieve values from EditText fields
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            // Add validation or further processing as needed
            if (!title.isEmpty() && !description.isEmpty() && !country.isEmpty() && !latitude.equals(0.0) && !longitude.equals(0.0)) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(uri.getLastPathSegment());
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTravel.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri urlImage = uriTask.getResult();
                        imageURL = urlImage.toString();
                        Travel travel = new Travel(title,description,country,imageURL,latitude,longitude);
                        FirebaseDatabase.getInstance().getReference("Travel").child(title).setValue(travel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(AddTravel.this, "Travel saved with success !", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(AddTravel.this, Home.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddTravel.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });;
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                    }
                });
                //Toast.makeText(this, "CREATE button clicked\nTitle: " + title + "\nDescription: " + description, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter title and description and select emplacement", Toast.LENGTH_SHORT).show();
            }
        }

           @Override
           protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
               super.onActivityResult(requestCode, resultCode, data);
               if (requestCode == 0000){
                   if (resultCode == RESULT_OK){
                       String title = data.getStringExtra("title");
                       String description = data.getStringExtra("description");
                       String country = data.getStringExtra("country");
                       Double latitude = data.getDoubleExtra("latitude", 0.0);
                       Double longitude = data.getDoubleExtra("longitude", 0.0);
                       titleEditText.setText(title);
                       descriptionEditText.setText(description);
                       this.title = title;
                       this.description = description;
                       this.country = country;
                       this.latitude = latitude;
                       this.longitude = longitude;
                   }
               }
           }
       }
