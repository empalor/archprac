package com.omri.countriesapp.view.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omri.countriesapp.R;
import com.omri.countriesapp.databinding.FragmentCountryListBinding;
import com.omri.countriesapp.db.entity.CountryEntity;
import com.omri.countriesapp.model.Country;
import com.omri.countriesapp.view.adapter.CountriesAdapter;
import com.omri.countriesapp.view.callback.CountryClickCallback;
import com.omri.countriesapp.viewmodel.CountryListViewModel;

import java.util.List;

/**
 * Created by omricohen294 on 05/09/2017.
 */

public class CountryListFragment extends LifecycleFragment {
    public static final String TAG = "CountryListFragment";
    public static final String KEY_COUNTRY_NAME = "country_name";

    private CountriesAdapter mCountriesAdapter;
    private FragmentCountryListBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_country_list, container, false);

        mCountriesAdapter = new CountriesAdapter(mCountryClickCallback);
        mBinding.countryList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mBinding.countryList.setAdapter(mCountriesAdapter);
        mBinding.setIsLoading(true);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Persisting ViewModel through configuration changes etc..
        final CountryListViewModel viewModel =
                ViewModelProviders.of(this).get(CountryListViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(CountryListViewModel viewModel) {
        // Update the list of countries when the data changes
        viewModel.getCountries().observe(this, new Observer<List<CountryEntity>>() {
            @Override
            public void onChanged(@Nullable List<CountryEntity> countries) {
                if (countries != null && countries.size() >= 1) {
                    // Update UI using the fragment mBinding
                    mCountriesAdapter.setCountryList(countries);
                    mBinding.setIsLoading(false);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }

    private final CountryClickCallback mCountryClickCallback = new CountryClickCallback() {

        // Selected country is passed upon mBinding by adapter (CountriesAdapter)
        @Override
        public void onClick(Country country) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(country);
            }
        }
    };
}
