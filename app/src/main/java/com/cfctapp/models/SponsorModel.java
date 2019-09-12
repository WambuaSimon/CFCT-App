package com.cfctapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sponsor")
public class SponsorModel {
    @PrimaryKey(autoGenerate = true)
    public  int id;

    public  String name;
    public  String country;
    public  String monthlyAmount;

    public SponsorModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMonthlyAmount() {
        return monthlyAmount;
    }

    public void setMonthlyAmount(String monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }
}
