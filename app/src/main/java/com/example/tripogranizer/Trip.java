package com.example.tripogranizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trip {
    public String id;


    public String name;
    public List<String> emails = new ArrayList<String>();

    public Trip(){}

    public Trip(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
