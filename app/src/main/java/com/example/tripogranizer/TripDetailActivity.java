package com.example.tripogranizer;

import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.TextView;
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

    DrawerLayout drawerLayout;
    Button AddUserBTN;
    Button ShoppingListBTN;
    Button CostListBTN;
    Button BillSettlement;
    Button AddCostsBTN;
    Button EditInformationBTN;
    TextView descriptionTV;

    Trip tripWybrany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        Intent intent = getIntent();
        String tripId = intent.getStringExtra("id");

        drawerLayout = findViewById(R.id.drawer_layout);

        AddUserBTN = findViewById(R.id.add_user_to_trip_btn);
        ShoppingListBTN = findViewById(R.id.shopping_list_btn);
        CostListBTN = findViewById(R.id.cost_list_btn);
        BillSettlement = findViewById(R.id.bill_settlement_btn);
        AddCostsBTN = findViewById(R.id.add_costs_to_trip_btn);
        EditInformationBTN = findViewById(R.id.edit_information_btn);
        descriptionTV = findViewById(R.id.activity_trip_detail_descriptionTV);



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Trips");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Trip trip = snapshot.getValue(Trip.class);
                    if (trip.id.equals(tripId)) {
                        tripWybrany = trip;
                    }
                }
                descriptionTV.setText(tripWybrany.description);
                //CODE

                AddUserBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TripDetailActivity.this, AddUsersActivity.class);
                        intent.putExtra("id", tripWybrany.id);
                        startActivity(intent);
                    }
                });
                ShoppingListBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TripDetailActivity.this, ShoppingListActivity.class);
                        intent.putExtra("id", tripWybrany.id);
                        startActivity(intent);
                    }
                });
                CostListBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TripDetailActivity.this, CostsActivity.class);
                        intent.putExtra("id", tripWybrany.id);
                        startActivity(intent);
                    }
                });
                BillSettlement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TripDetailActivity.this, BillSettlement.class);
                        intent.putExtra("id", tripWybrany.id);
                        startActivity(intent);
                    }
                });
                AddCostsBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TripDetailActivity.this, AddCostsActivity.class);
                        intent.putExtra("id", tripWybrany.id);
                        startActivity(intent);
                    }
                });
                EditInformationBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TripDetailActivity.this, EditDescriptionActivity.class);
                        intent.putExtra("id", tripWybrany.id);
                        startActivity(intent);
                    }
                });
                //END CODE
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        recreate();
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