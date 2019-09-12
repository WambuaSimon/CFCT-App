package com.cfctapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cfctapp.models.ChildModel;
import com.cfctapp.models.SponsorModel;

import java.util.List;

@Dao
public interface SponsorDao {

    @Query("Select * from sponsor")
    List<SponsorModel> getSponsor();

    @Insert
    void insertSponsor(SponsorModel sponsorModel);

    @Update
    void updateSponsor(SponsorModel sponsorModel);

    @Delete
    void deleteSponsor(SponsorModel sponsorModel);

}
