package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class TripDetailActivity extends AppCompatActivity {

    private ListView listView;
    DrawerLayout drawerLayout;
    Button AddUser;
    EditText UserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        Intent intent = getIntent();
        String tripId = intent.getStringExtra("id");

        listView = findViewById(R.id.triplistuser);
        AddUser = findViewById(R.id.add_user_to_trip_btn);
        drawerLayout = findViewById(R.id.drawer_layout);
        UserEmail = findViewById(R.id.register_user_email);

        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.item_user, list);
        listView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Trip trip = snapshot.getValue(Trip.class);
                    if (trip.id.equals(tripId)) {
                        Toast.makeText(TripDetailActivity.this, "NAZWA TRIPA :" + trip.name,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AddUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Trip trip = snapshot.getValue(Trip.class);
                            String email = UserEmail.getText().toString();
                            Log.d("chuj", trip.emails.toString());
                            trip.emails.add(email);
                            FirebaseDatabase.getInstance().getReference().child("Trips").child(trip.id).setValue(trip);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });


//        AddUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
//                reference.addValueEventListener(new ValueEventListener() {
//                    @SuppressLint("RestrictedApi")
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Map<String , String> map = (Map)snapshot.getValue();
//                            String pole = UserEmail.getText().toString();
//                            String baza = map.get("email");
//                            Trip trip = snapshot.getValue(Trip.class);
//                            String email = UserEmail.getText().toString();
//                            if(pole.equals(baza)) {
//                                trip.emails.add(email);
//                                FirebaseDatabase.getInstance().getReference().child("Trips").child(trip.id).setValue(trip);
//                            }
//                            else
//                                Toast.makeText(TripDetailActivity.this, "There is no such user", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });
//            }
//        });

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
        recreate();
    }

    public void ClickAddTrip(View view){
        AfterLoginActivity.redirectActivity(this, AddTripActivity.class);
    }

    public void ClickLogout(View view){
        AfterLoginActivity.logout(this);
    }

    public void ClickImage(View view){
        AfterLoginActivity.redirectActivity(this, AddCostsActivity.class);
    }

    protected void onPause() {
        super.onPause();
        AfterLoginActivity.closeDrawer(drawerLayout);
    }
}