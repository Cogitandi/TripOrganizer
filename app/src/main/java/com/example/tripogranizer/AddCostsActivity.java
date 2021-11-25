package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.ContentResolver;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddCostsActivity extends AppCompatActivity {

    Cost cost = new Cost();
    DrawerLayout drawerLayout;
    Button ch,up,save;
    ImageView img;
    StorageReference mStorageRef;
    EditText costs1;
    public Uri imguri;
    String uploadedUri;
    private StorageTask uploadTask;
    Trip tripWybrany;
    // LIST
    TextView selectItemList;
    List<Shopping> selectedItems = new ArrayList<Shopping>();
    boolean[] selectedItemsCheck;
    // END LIST
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_costs);

        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

        setContentView(R.layout.activity_add_costs);
        ch = findViewById(R.id.add_photo_btn);
        up = findViewById(R.id.upload_photo_btn);
        img = findViewById(R.id.imagePhoto);
        save = findViewById(R.id.add_costs_btn);
        costs1 = findViewById(R.id.register_costs);
        // LIST
        selectItemList = findViewById(R.id.activity_add_costs_select_items);
        selectItemList = findViewById(R.id.activity_add_costs_select_items);
        //
        drawerLayout = findViewById(R.id.drawer_layout);

        Intent intent = getIntent();
        String tripId = intent.getStringExtra("id");
        Log.d("TEST",tripId);
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

                /// SELECT LIST
                selectedItemsCheck = new boolean[tripWybrany.items.size()];
                selectItemList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Initialize alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddCostsActivity.this);

                        // set title
                        builder.setTitle("Wybierz przedmioty");

                        // set dialog non cancelable
                        builder.setCancelable(false);
                        List<String> listItems = new ArrayList<>();
                        for(Shopping item : tripWybrany.items) {
                            if(!item.bought) {
                                listItems.add(item.name);
                            }

                        }
                        builder.setMultiChoiceItems(listItems.toArray(new String[0]), selectedItemsCheck, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                List<Shopping> itemsToSelect = new ArrayList<>();
                                for(Shopping item: tripWybrany.items) {
                                    if(item.bought==false) {
                                        itemsToSelect.add(item);
                                    }
                                }
                                // check condition
                                if (b) {
                                    // when checkbox selected
                                    // Add position  in lang list
                                    selectedItems.add(itemsToSelect.get(i));
                                    // Sort array list
                                    //Collections.sort(langList);
                                } else {
                                    // when checkbox unselected
                                    // Remove position from langList
                                    selectedItems.remove(itemsToSelect.get(i));
                                }
                            }
                        });


                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Initialize string builder
                                StringBuilder stringBuilder = new StringBuilder();
                                // use for loop
                                for (int j = 0; j < selectedItems.size(); j++) {
                                    // concat array value
                                    stringBuilder.append(selectedItems.get(j).name);
                                    // check condition
                                    if (j != selectedItems.size() - 1) {
                                        // When j value  not equal
                                        // to lang list size - 1
                                        // add comma
                                        stringBuilder.append(", ");
                                    }
                                }
                                // set text on textView
                                selectItemList.setText(stringBuilder.toString());
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // dismiss dialog
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // use for loop
                                for (int j = 0; j < selectedItemsCheck.length; j++) {
                                    // remove all selection
                                    selectedItemsCheck[j] = false;
                                    // clear language list
                                    selectedItems.clear();
                                    // clear text view value
                                    selectItemList.setText("");
                                }
                            }
                        });
                        // show dialog
                        builder.show();
                    }
                });
                // END SELECT LIST

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /// START CODE

        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(AddCostsActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }else
                Fileuploader();
            }
        });



            save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cost.price =  Float.valueOf(costs1.getText().toString());
                cost.userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                cost.items = selectedItems;
                cost.Image = uploadedUri;
                for(Shopping item : selectedItems) {
                    item.bought = true;
                    item.boughtBy = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                }
                tripWybrany.costs.add(cost);
                FirebaseDatabase.getInstance().getReference().child("Trips").child(tripWybrany.id).setValue(tripWybrany);

                Intent intent = new Intent(AddCostsActivity.this, ShoppingListActivity.class);
                intent.putExtra("id", tripWybrany.id);
                startActivity(intent);
            }
        });

    }


    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void Fileuploader() {
        String filename = System.currentTimeMillis()+"."+getExtension(imguri);
        uploadedUri = filename;
        StorageReference Ref = mStorageRef.child(filename);
        uploadTask = Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddCostsActivity.this,"Image Uploaded sucessFully",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void Filechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imguri = data.getData();
            img.setImageURI(imguri);
        }
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