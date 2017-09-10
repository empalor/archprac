package com.omri.countriesapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.omri.countriesapp.db.converter.BordersConverter;
import com.omri.countriesapp.db.dao.CountryDao;
import com.omri.countriesapp.db.entity.CountryEntity;

/**
 * Created by omricohen294 on 05/09/2017.
 */

@Database(entities = {CountryEntity.class}, version = 1)
@TypeConverters(BordersConverter.class)
public abstract class AppDatabase extends RoomDatabase{
    static final String DATABASE_NAME = "countries_db";

    public abstract CountryDao countryDao();
}
