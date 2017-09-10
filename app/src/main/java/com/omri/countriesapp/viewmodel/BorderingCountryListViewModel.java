package com.omri.countriesapp.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.omri.countriesapp.db.DatabaseCreator;
import com.omri.countriesapp.db.entity.CountryEntity;
import com.omri.countriesapp.repository.CountryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omricohen294 on 05/09/2017.
 */

public class BorderingCountryListViewModel extends AndroidViewModel {

    private LiveData<List<CountryEntity>> mObservableCountryList;

    public BorderingCountryListViewModel(final Application application, final List<String> borderingAlphaThreeCodes) {
        super(application);

        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());
        mObservableCountryList = CountryRepository.
                getInstance(databaseCreator).
                getBorderingCountryList(borderingAlphaThreeCodes);
    }

    /**
     * Expose the LiveData bordering Countries query so the UI can observe it.
     */
    public LiveData<List<CountryEntity>> getBorderingCountries() {
        return mObservableCountryList;
    }

    /**
     * A creator is used to inject the country's bordering countries alpha3codes into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final ArrayList<String> borderingCountriesCodes;

        public Factory(@NonNull Application application, ArrayList<String> borderingCodes) {
            this.application = application;
            this.borderingCountriesCodes = borderingCodes;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new BorderingCountryListViewModel(application, borderingCountriesCodes);
        }
    }
}
