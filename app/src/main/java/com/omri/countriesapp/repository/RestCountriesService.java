package com.omri.countriesapp.repository;

import com.omri.countriesapp.db.entity.CountryEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by omricohen294 on 05/09/2017.
 */

public interface RestCountriesService {
    // Rest api's endpoint
    String HTTPS_API_RESTCOUNTRIES_URL = "https://restcountries.eu/";

    @GET("rest/v2/all?fields=name;nativeName;alpha3Code;borders")
    Call<List<CountryEntity>> getCountryListFiltered();
}
