package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BillSettlement extends AppCompatActivity {

    Trip tripWybrany;
    TextView totalCostTV;
    TextView costPerUserTV;
    ListView settlementDetailsGetLV;
    ListView settlementDetailsGiveLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_settlement);

        totalCostTV = findViewById(R.id.activity_bill_settlement_totalCostTV);
        costPerUserTV = findViewById(R.id.activity_bill_settlement_costPerUserTV);
        settlementDetailsGetLV = findViewById(R.id.activity_bill_settlement_settlementDetailsGetLV);
        settlementDetailsGiveLV = findViewById(R.id.activity_bill_settlement_settlementDetailsGiveLV);


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

                /// START
                float cost = tripWybrany.GetTotalCost();
                float costPerUser = tripWybrany.GetCostPerUser();
                totalCostTV.setText(String.valueOf(cost));
                costPerUserTV.setText(String.valueOf(costPerUser));

                List<Refund> refunds = new ArrayList<>();
                List<String> displayItems = new ArrayList<>();
                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                for(Refund item: tripWybrany.RefundList()) {
                    if(item.ToUserEmail.equals(currentUser)) {
                        refunds.add(item);
                        String res = "OD: " + item.FromUserEmail + " | Kwota: " + item.Value + "zł";
                        displayItems.add(res);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BillSettlement.this,R.layout.support_simple_spinner_dropdown_item,displayItems);
                settlementDetailsGetLV.setAdapter(adapter);

                List<Refund> give = new ArrayList<>();
                List<String> displayItemsGive = new ArrayList<>();
                for(Refund item: tripWybrany.RefundList()) {
                    if(item.FromUserEmail.equals(currentUser)) {
                        give.add(item);
                        String res = "Dla: " + item.ToUserEmail + " | Kwota: " + item.Value + " zł";
                        displayItemsGive.add(res);
                    }
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(BillSettlement.this,R.layout.support_simple_spinner_dropdown_item,displayItemsGive);
                settlementDetailsGiveLV.setAdapter(adapter2);




                // END

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}