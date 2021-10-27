package com.example.tripogranizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(MainActivity.this , LoginActivity.class));

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Toast.makeText(MainActivity.this,"ZALOGOWANY",Toast.LENGTH_LONG);

        } else {
            Toast.makeText(MainActivity.this,"NIE ZALOGOWANY",Toast.LENGTH_LONG);
        }
    }
}