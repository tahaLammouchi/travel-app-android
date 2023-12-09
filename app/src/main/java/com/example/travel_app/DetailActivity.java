package com.example.travel_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
public class DetailActivity extends AppCompatActivity {
    TextView description, title, country;
    ImageView image;
    Button gotomap;
    String key = "";
    String imageUrl = "";
    Double latitude,longitude;

    //FloatingActionButton deleteButton, editButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        description = findViewById(R.id.detail_description);
        image = findViewById(R.id.detail_image);
        title = findViewById(R.id.detail_title);
        country = findViewById(R.id.detail_title);
        gotomap = findViewById(R.id.detail_map);

        //There some static values for tests
        description.setText("Espagne is a good country Espagne is a good country Espagne is a good country Espagne is a good country Espagne is a good country Espagne is a good country");
        title.setText("Travel to Espagne");
        country.setText("Espagne");
        key = "key1";
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/travel-app-100f6.appspot.com/o/Android%20Images%2F1000006051?alt=media&token=9c39b3fe-d2a1-4332-b382-3d9be9cf3241";
        longitude = 40.05268232705773;
        longitude = 4.186117239296436;
        Glide.with(this).load(imageUrl).into(image);

        // Here i get the values from badis intent
        /*
        Intent intent = getIntent();
        if (intent != null){
            description.setText(intent.getStringExtra("Description"));
            title.setText(intent.getStringExtra("Title"));
            country.setText(intent.getStringExtra("Country"));
            key = intent.getStringExtra("Title");
            imageUrl = intent.getStringExtra("Image");
            longitude = intent.getDoubleExtra("Latitude", 0.0)
            longitude = intent.getDoubleExtra("Longitude", 0.0)
            Glide.with(this).load(imageUrl).into(image);
        }
     */

        // Here its delete logic from the tutorial , i didnt implement it in xml do it your way
       /* deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Travel");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }
        });

        // Here its edit logic from the tutorial , i didnt implement it in xml do it your way
        // Maybe here you wanna use AddTravel (activity) instead of DetailActivity to show the form of edit
        // Think on it <3
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Title", title.getText().toString())
                        .putExtra("Description", description.getText().toString())
                        .putExtra("Country", country.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        }); */
        gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, GoToMap.class);
                i.putExtra("Latitude", latitude);
                i.putExtra("Longitude", longitude);
                startActivity(i);
            }
        });
    }
}