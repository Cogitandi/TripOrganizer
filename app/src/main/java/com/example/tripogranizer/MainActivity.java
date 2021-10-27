package com.example.tripogranizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
        //setContentView(R.layout.activity_after_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(MainActivity.this , LoginActivity.class));

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            AfterLoginActivity.redirectActivity(this, AfterLoginActivity.class);
            Toast.makeText(MainActivity.this,"ZALOGOWANY",Toast.LENGTH_LONG);
            

        } else {
            Toast.makeText(MainActivity.this,"NIE ZALOGOWANY",Toast.LENGTH_LONG);
        }
    }
}