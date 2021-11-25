package com.example.tripogranizer;

import android.net.Uri;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class Cost {
    public String userEmail;
    public float price;
    public List<Shopping> items = new ArrayList<>();
    public String Image;
}
