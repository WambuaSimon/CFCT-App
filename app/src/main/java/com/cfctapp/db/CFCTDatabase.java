package com.cfctapp.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.cfctapp.dao.ChildDao;
import com.cfctapp.dao.SponsorDao;
import com.cfctapp.models.ChildModel;
import com.cfctapp.models.SponsorModel;

@Database(entities = {ChildModel.class, SponsorModel.class}, version = 4,exportSchema = false)
public abstract class CFCTDatabase extends RoomDatabase {

    private static final String DB_NAME = "cfct_db";
    static CFCTDatabase cfctDatabase;

  public static synchronized CFCTDatabase getCfctDatabase(Context context) {
        if (cfctDatabase == null) {
            cfctDatabase = Room.databaseBuilder(context.getApplicationContext(), CFCTDatabase.class, DB_NAME).fallbackToDestructiveMigration()
                    .build();
        }
        return cfctDatabase;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    public abstract ChildDao childDao();
    public abstract SponsorDao sponsorDao();
}
