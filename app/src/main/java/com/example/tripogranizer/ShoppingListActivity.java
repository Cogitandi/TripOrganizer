package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Button Add;
    ListView itemsList;
    Trip tripWybrany;

    Button allBTN;
    Button boughtableBTN;
    List<Shopping> shoppingItems;
    List<String> nazwy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        drawerLayout = findViewById(R.id.drawer_layout);
        Add = findViewById(R.id.add_shopping_list_btn);
        itemsList = findViewById(R.id.activity_shopping_list);

        allBTN = findViewById(R.id.activity_shopping_list_allBTN);
        boughtableBTN = findViewById(R.id.activity_shopping_list_boughtableBTN);

        allBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nazwy = new ArrayList<>();
                for(Shopping item: shoppingItems) {


                            String out;
                            out = item.name + "," + item.number + " szt.";
                            if(item.bought) {
                                out += " - kupione przez: " + item.boughtBy;
                            }
                            nazwy.add(out);


                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShoppingListActivity.this,R.layout.support_simple_spinner_dropdown_item,nazwy);
                itemsList.setAdapter(adapter);
                Add.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShoppingListActivity.this, AddingToShoppingListActivity.class);
                        intent.putExtra("id", tripWybrany.id);
                        startActivity(intent);
                    }
                });
            }
        });
        boughtableBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nazwy = new ArrayList<>();
                for(Shopping item: shoppingItems) {
                    if(!item.bought) {
                        String out;
                        out = item.name + "," + item.number + " szt.";
                        if(item.bought) {
                            out += " - kupione przez: " + item.boughtBy;
                        }
                        nazwy.add(out);

                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShoppingListActivity.this,R.layout.support_simple_spinner_dropdown_item,nazwy);
                itemsList.setAdapter(adapter);
                Add.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShoppingListActivity.this, AddingToShoppingListActivity.class);
                        intent.putExtra("id", tripWybrany.id);
                        startActivity(intent);
                    }
                });
            }
        });


        Intent intent = getIntent();
        String tripId = intent.getStringExtra("id");
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

                //CODE
                nazwy = new ArrayList<>();
                shoppingItems = new ArrayList<>();
                for(Shopping item : tripWybrany.items) {
                    shoppingItems.add(item);
                    String out;
                    out = item.name + "," + item.number + " szt.";
                    if(item.bought) {
                        out += " - kupione przez: " + item.boughtBy;
                    }
                    nazwy.add(out);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShoppingListActivity.this,R.layout.support_simple_spinner_dropdown_item,nazwy);
                itemsList.setAdapter(adapter);
                itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Shopping s = shoppingItems.get(position);

                        AlertDialog.Builder alert = new AlertDialog.Builder(ShoppingListActivity.this);
                        alert.setTitle("Usun przedmiot");
                        alert.setMessage("Czy chcesz usunac przedmiot?");
                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                tripWybrany.items.remove(s);
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
                Add.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShoppingListActivity.this, AddingToShoppingListActivity.class);
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
        AfterLoginActivity.redirectActivity(this, TripActivity.class);
    }

    public void ClickAddTrip(View view){
        AfterLoginActivity.redirectActivity(this, AddTripActivity.class);
    }

    public void ClickLogout(View view){
        AfterLoginActivity.logout(this);
    }

    public void ClickShopping(View view){
        recreate();
    }

    protected void onPause() {
        super.onPause();
        AfterLoginActivity.closeDrawer(drawerLayout);
    }
}