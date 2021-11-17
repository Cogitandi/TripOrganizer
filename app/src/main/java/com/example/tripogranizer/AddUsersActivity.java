package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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

import java.util.ArrayList;
import java.util.List;

public class AddUsersActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Button AddUser;
    Trip tripWybrany;
    EditText UserEmail;
    private ListView listView;
    public List<Trip> trips = new ArrayList<Trip>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);

        ArrayList<Trip> list = new ArrayList<Trip>();
        TripAdapter adapter = new TripAdapter(this, list);

        Intent intent = getIntent();
        String tripId = intent.getStringExtra("id");

        AddUser = findViewById(R.id.add_users_to_trip_btn);
        drawerLayout = findViewById(R.id.drawer_layout);
        UserEmail = findViewById(R.id.register_user_email);

        listView.setAdapter(adapter);

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Trip trip = snapshot.getValue(Trip.class);
//                    if (trip.id.equals(tripId)) {
//                        tripWybrany = trip;
//                        Toast.makeText(AddUsersActivity.this, "NAZWA TRIPA :" + trip.name,
//                                Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Log.d("Z bazy",snapshot.toString());
                    Trip trip = snapshot.getValue(Trip.class);
                    if (trip.id.equals(tripId)) {
                        tripWybrany = trip;
                        Toast.makeText(AddUsersActivity.this, "NAZWA TRIPA :" + trip.name,
                                Toast.LENGTH_LONG).show();
                    }
                    if (tripWybrany.emails.contains(email)) {

                        list.add(trip);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AddUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = UserEmail.getText().toString();
                tripWybrany.emails.add(email);
                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripWybrany.id).setValue(tripWybrany);
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