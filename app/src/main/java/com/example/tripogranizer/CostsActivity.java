package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class CostsActivity extends AppCompatActivity {

    Trip tripWybrany;
    ListView costsLV;
    StorageReference mStorageRef;
    List<String> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs);

        costsLV = findViewById(R.id.activity_costs_costsLV);


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
                listItems = new ArrayList<>();

                for(Cost item: tripWybrany.costs) {
                    String value = "Zakupione przez: " + item.userEmail + " | koszt: " + item.price + " PLN";
                    listItems.add(value);
                }
                /// START CODE
                ArrayAdapter adapter = new ArrayAdapter<String>(CostsActivity.this,R.layout.support_simple_spinner_dropdown_item,listItems);
                costsLV.setAdapter(adapter);
                costsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // TUTAJ KOD
                        Cost cost = tripWybrany.costs.get(position);
                        if(cost.Image==null) {
                            return;
                        }

                        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
                        StorageReference Ref = mStorageRef.child(cost.Image);
                        //
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        //StorageReference photoReference= storageReference.child("pictures/photo1.jpg");

                        final long ONE_MEGABYTE = 1024 * 1024;
                        Ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Dialog settingsDialog = new Dialog(CostsActivity.this);
                                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                View image = getLayoutInflater().inflate(R.layout.cost_image_layout, null);
                                ImageView img = image.findViewById(R.id.cost_image_IV);
                                Button btn = image.findViewById(R.id.XDDD);
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        settingsDialog.hide();
                                    }
                                });

                                settingsDialog.setContentView(image);
                                settingsDialog.show();
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                img.setImageBitmap(bmp);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                            }
                        });
                        //

                        // END KOD

                    }
                });
                // END

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}