package com.example.tripogranizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<Trip> {
    public UserAdapter(Context context, ArrayList<Trip> trips) {
        super(context, 0, trips);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trip trip = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_listview_users, parent, false);
        }
        TextView tripName = (TextView) convertView.findViewById(R.id.userName);
        tripName.setText((CharSequence) trip.emails);

        return convertView;
    }
}
