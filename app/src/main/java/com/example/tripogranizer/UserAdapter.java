package com.example.tripogranizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<FirebaseUser> {
    public UserAdapter(Context context, ArrayList<FirebaseUser> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FirebaseUser user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_listview_users, parent, false);
        }
        TextView tripName = (TextView) convertView.findViewById(R.id.userName);
        tripName.setText((CharSequence) user.getEmail());

        return convertView;
    }
}
