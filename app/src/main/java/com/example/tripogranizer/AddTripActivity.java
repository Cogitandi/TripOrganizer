package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class AddTripActivity extends AppCompatActivity {

    DatabaseReference ref;
    EditText nameT, nameU;
    Button btn, btnUser;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        drawerLayout = findViewById(R.id.drawer_layout);

        nameT = findViewById(R.id.register_trip_name);
        btn = findViewById(R.id.add_trip_btn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = nameT.getText().toString();
                if(txt_name.isEmpty()){
                    Toast.makeText(AddTripActivity.this, "No name entered!",Toast.LENGTH_SHORT).show();
                } else {
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    Trip trip = new Trip(txt_name);
                    trip.emails.add(email);
                    trip.id = UUID.randomUUID().toString();
                    FirebaseDatabase.getInstance().getReference().child("Trips").child(trip.id).setValue(trip);

                    Toast.makeText(AddTripActivity.this, "Trip has been created. Now please add users to trip.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddTripActivity.this, TripActivity.class));
                }
            }
        });

    }

    public void ClickMenu(View view){
        AfterLoginActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        AfterLoginActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        AfterLoginActivity.redirectActivity(this, AfterLoginActivity.class);
    }

    public void ClickTrip(View view){
        AfterLoginActivity.redirectActivity(this, TripActivity.class);
    }

    public void ClickAddTrip(View view){
        recreate();
    }

    public void ClickShopping(View view){
        AfterLoginActivity.redirectActivity(this,ShoppingListActivity.class);
    }

    public void ClickLogout(View view){
        AfterLoginActivity.logout(this);
    }

    protected void onPause() {
        super.onPause();
        AfterLoginActivity.closeDrawer(drawerLayout);
    }

}