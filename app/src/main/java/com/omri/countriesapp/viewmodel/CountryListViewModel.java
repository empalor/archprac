package com.omri.countriesapp.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.omri.countriesapp.db.DatabaseCreator;
import com.omri.countriesapp.db.entity.CountryEntity;
import com.omri.countriesapp.repository.CountryRepository;

import java.util.List;

/**
 * Created by omricohen294 on 05/09/2017.
 */

public class CountryListViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();

    static {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private LiveData<List<CountryEntity>> mObservableCountryList;

    public CountryListViewModel(final Application application) {
        super(application);

        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());

        LiveData<Boolean> databaseCreated = databaseCreator.isDatabaseCreated();
        mObservableCountryList = Transformations.switchMap(databaseCreated,
                new Function<Boolean, LiveData<List<CountryEntity>>>() {
                    @Override
                    public LiveData<List<CountryEntity>> apply(Boolean isDbCreated) {
                        if (!Boolean.TRUE.equals(isDbCreated)) {
                            //noinspection unchecked
                            return ABSENT;
                        } else {
                            return CountryRepository.getInstance(databaseCreator).getCountryList();
                        }
                    }
                });

        databaseCreator.createDb(this.getApplication());
    }

    /**
     * Expose the LiveData Countries query so the UI can observe it.
     */
    public LiveData<List<CountryEntity>> getCountries() {
        return mObservableCountryList;
    }
}
