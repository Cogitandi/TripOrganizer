package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

        listView = findViewById(R.id.triplistuser);

        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item,list);

        Intent intent = getIntent();
        String tripId = intent.getStringExtra("id");

        AddUser = findViewById(R.id.add_users_to_trip_btn);
        drawerLayout = findViewById(R.id.drawer_layout);
        UserEmail = findViewById(R.id.register_user_email);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email = list.get(position);

                AlertDialog.Builder alert = new AlertDialog.Builder(AddUsersActivity.this);
                alert.setTitle("Usun uzytkownika");
                alert.setMessage("Czy chcesz usunac uzytkownika?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        tripWybrany.emails.remove(email);
                        FirebaseDatabase.getInstance().getReference().child("Trips").child(tripWybrany.id).setValue(tripWybrany);
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });

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
                        for(String email : trip.emails){
                            list.add(email);
                        }
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

    public void ClickShopping(View view){
        AfterLoginActivity.redirectActivity(this,ShoppingListActivity.class);
    }

    protected void onPause() {
        super.onPause();
        AfterLoginActivity.closeDrawer(drawerLayout);
    }
}