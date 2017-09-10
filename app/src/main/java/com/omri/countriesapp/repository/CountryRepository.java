package com.omri.countriesapp.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.omri.countriesapp.db.DatabaseCreator;
import com.omri.countriesapp.db.dao.CountryDao;
import com.omri.countriesapp.db.entity.CountryEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by omricohen294 on 05/09/2017.
 */

public class CountryRepository {
    private static CountryRepository sInstance;

    private RestCountriesService mRestCountriesService;
    private CountryDao mCountryDao = null;

    private CountryRepository(DatabaseCreator dbCreator) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestCountriesService.HTTPS_API_RESTCOUNTRIES_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestCountriesService = retrofit.create(RestCountriesService.class);

        this.mCountryDao = dbCreator.getDatabase().countryDao();
    }

    public synchronized static CountryRepository getInstance(DatabaseCreator dbCreator) {
        if (sInstance == null) {
            sInstance = new CountryRepository(dbCreator);
        }
        return sInstance;
    }

    public LiveData<List<CountryEntity>> getCountryList() {
        mRestCountriesService.getCountryListFiltered().enqueue(new Callback<List<CountryEntity>>() {
            @Override
            public void onResponse(@NonNull Call<List<CountryEntity>> call, @NonNull Response<List<CountryEntity>> response) {
                simulateDelay();
                new InsertToDbAsync(mCountryDao).execute(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<CountryEntity>> call, @NonNull Throwable t) {
            }
        });

        return mCountryDao.loadAllCountries();
    }

    public LiveData<List<CountryEntity>> getBorderingCountryList(List<String> bordersAlphaThree) {
        return mCountryDao.loadByName(bordersAlphaThree);
    }

    // Access to DB has to be done from a worker thread, so here we are..
    static class InsertToDbAsync extends AsyncTask<Object, Void, Void> {
        CountryDao countryDao;

        public InsertToDbAsync(CountryDao countryDao) {
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Object... countries) {

            List<CountryEntity> countriesToInsert = (List<CountryEntity>) countries[0];
            countryDao.insertAll(countriesToInsert);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
