package com.cfctapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

//@Entity(foreignKeys = @ForeignKey(entity = SponsorModel.class,
//        parentColumns = "id",
//        childColumns = "sponsorId",
//        onDelete = CASCADE,
//        onUpdate = CASCADE), indices = {@Index("sponsorId")})

@Entity(tableName = "ChildModel")
public class ChildModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public  String name;
    public String country;
    public String hobby;
    public int age;
    public String sponsor;

    public ChildModel() {
    }


    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
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

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
