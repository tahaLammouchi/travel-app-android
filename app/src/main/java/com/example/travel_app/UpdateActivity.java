package com.example.travel_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travel_app.entities.Travel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateActivity extends AppCompatActivity {

    ImageView travel_img;
    EditText title_input, description_input;
    String title="", description="",country="";
    Button select_emplacement_btn, update_btn;

    Double latitude, longitude;

    String imageUrl;
    String key, oldImageURL = "";
    Uri uri;
    DatabaseReference databaseReference = null;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title_input = findViewById(R.id.title_input);
        description_input = findViewById(R.id.description_input);
        travel_img = findViewById(R.id.travel_img);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Toast.makeText(UpdateActivity.this, "title : " + bundle.getString("Title"), Toast.LENGTH_LONG).show();
            title_input.setText(bundle.getString("Title"));
            description_input.setText(bundle.getString("Description"));
            oldImageURL = bundle.getString("Image");
            Glide.with(this).load(oldImageURL).into(travel_img);

            //// because its always empty (i need to go back to the root that sets it and give it value
            // WAIT I THINK I KNOW WHY , BECAUSE THE OLD DATA INSIDE FIREBASE DIDN't HAVE COUNTRY ?
            if(bundle.getString("Country") != null)
                country =  bundle.getString("Country");
            key = bundle.getString("Key");
            longitude = bundle.getDouble("Longitude");
            latitude = bundle.getDouble("Latitude");

        }
        update_btn = findViewById(R.id.update_btn);
        select_emplacement_btn = findViewById(R.id.select_emplacement_btn);
        select_emplacement_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle SELECT EMPLACEMENT button click
                onSelectEmplacementButtonClick();
            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            travel_img.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        travel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    private void onSelectEmplacementButtonClick() {
        String title = title_input.getText().toString().trim();
        String description = description_input.getText().toString().trim();

        Intent i = new Intent(UpdateActivity.this, Maps.class);
        i.putExtra("title", title);
        i.putExtra("description", description);
        startActivityForResult(i, 0000);
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
                title_input.setText(title);
                description_input.setText(description);
                this.title = title;
                this.description = description;
                if(country != null && country != "")
                    this.country = country;
                this.latitude = latitude;
                this.longitude = longitude;
            }
        }
    }
    public void saveData(){
        storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
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
                imageUrl = urlImage.toString();
                updateData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void updateData(){
        title = title_input.getText().toString().trim();
        description = description_input.getText().toString().trim();
        Log.w("badis", title + "-" + description + "-" + country + "-" + latitude + "-" + longitude + "-" + imageUrl);
        Travel dataClass = new Travel(title, description,country, imageUrl, latitude, longitude);

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        Log.w("badis ", key);

        databaseReference = FirebaseDatabase.getInstance().getReference("Travel").child(key);
        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                }
                if(task.isComplete() || task.isCanceled()) {
                    dialog.dismiss();
                    //finish();
                    Intent i = new Intent(UpdateActivity.this, Home.class);
                    startActivity(i);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
}