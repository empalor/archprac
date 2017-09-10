package com.omri.countriesapp.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.omri.countriesapp.db.entity.CountryEntity;
import com.omri.countriesapp.model.Country;

import java.util.List;

/**
 * Created by omricohen294 on 05/09/2017.
 */

@Dao
public interface CountryDao {

    @Query("SELECT * FROM countries")
    LiveData<List<CountryEntity>> loadAllCountries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CountryEntity> countries);

    @Query("SELECT * FROM countries WHERE alpha3Code in (:bordersAlphaThree)")
    LiveData<List<CountryEntity>> loadByName(List<String> bordersAlphaThree);
}
