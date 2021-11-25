package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddingToShoppingListActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Button Add;
    EditText Item, Number;
    Trip tripWybrany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_to_shopping_list);

        drawerLayout = findViewById(R.id.drawer_layout);
        Add = findViewById(R.id.add_shopping_list_btn);
        Item = findViewById(R.id.add_item);
        Number = findViewById(R.id.add_number);

        Intent intent = getIntent();
        String tripId = intent.getStringExtra("id");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.d("Z bazy", snapshot.toString());
                    Trip trip = snapshot.getValue(Trip.class);

                    if (trip.id.equals(tripId)) {
                        tripWybrany = trip;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Shopping item = new Shopping();
                item.name = Item.getText().toString();
                item.number = Integer.valueOf(Number.getText().toString());
                tripWybrany.items.add(item);
                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripWybrany.id).setValue(tripWybrany);
                Intent intent = new Intent(AddingToShoppingListActivity.this, ShoppingListActivity.class);
                intent.putExtra("id", tripWybrany.id);
                startActivity(intent);
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
        AfterLoginActivity.redirectActivity(this, ShoppingListActivity.class);
    }

    protected void onPause() {
        super.onPause();
        AfterLoginActivity.closeDrawer(drawerLayout);
    }
}