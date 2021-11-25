package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDescriptionActivity extends AppCompatActivity {

    Trip tripWybrany;
    EditText descriptionET;
    Button saveBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);
        descriptionET = findViewById(R.id.activity_edit_description_textET);
        saveBTN = findViewById(R.id.activity_edit_description_saveBTN);

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripWybrany.description = descriptionET.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripWybrany.id).setValue(tripWybrany);
                Intent intent = new Intent(EditDescriptionActivity.this, TripDetailActivity.class);
                intent.putExtra("id", tripWybrany.id);
                startActivity(intent);
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

                /// START CODE
                    descriptionET.setText(tripWybrany.description);
                // END

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}