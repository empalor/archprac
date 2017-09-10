package com.omri.countriesapp.db.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.omri.countriesapp.db.converter.BordersConverter;
import com.omri.countriesapp.model.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omricohen294 on 05/09/2017.
 */

@Entity(tableName = "countries")
public class CountryEntity implements Country {

    @PrimaryKey
    private String name;

    // Members
    private String nativeName;
    private String alpha3Code;
    private List<String> borders;

    public CountryEntity(){}

    public CountryEntity(Country country) {
        this.name = country.getName();
        this.nativeName = country.getNativeName();
        this.alpha3Code = country.getAlpha3Code();
        this.borders = country.getBorders();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

    public void setBorders(List<String> borders) {
        this.borders = borders;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNativeName() {
        return nativeName;
    }

    @Override
    public String getAlpha3Code() {
        return alpha3Code;
    }

    @Override
    public List<String> getBorders() {
        return borders;
    }
}
