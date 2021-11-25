package com.example.tripogranizer;

public class Shopping {
    public String id;
    public String name;
    public int number;
    public boolean bought;
    public String boughtBy;


    public Shopping(){}

    public Shopping(String id, String name, int number) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.bought = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
