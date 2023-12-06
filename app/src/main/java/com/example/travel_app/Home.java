package com.example.travel_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    TextView homeText, userText;
    FirebaseAuth auth;
    Button logoutButton;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout);
        homeText  = findViewById(R.id.homeText);
        userText = findViewById(R.id.userText);
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish();
        }else{
            userText.setText(user.getEmail());
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}