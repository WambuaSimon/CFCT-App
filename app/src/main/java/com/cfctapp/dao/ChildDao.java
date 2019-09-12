package com.cfctapp.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cfctapp.models.ChildModel;

import java.util.List;

@Dao
public interface  ChildDao {

    @Query("Select * from ChildModel")
     List<ChildModel> getChildren();

    @Insert
     void insertChild(ChildModel childModel);

    @Update
     void updateChild(ChildModel childModel);

    @Delete
     void deleteChild(ChildModel childModel);

//    @Query("SELECT * FROM repo WHERE userId=:userId")
//    List<Repo> findRepositoriesForUser(final int userId);
}
