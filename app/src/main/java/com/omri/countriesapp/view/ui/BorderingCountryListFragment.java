package com.omri.countriesapp.view.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omri.countriesapp.R;
import com.omri.countriesapp.databinding.FragmentCountryListBinding;
import com.omri.countriesapp.db.entity.CountryEntity;
import com.omri.countriesapp.model.Country;
import com.omri.countriesapp.view.adapter.CountriesAdapter;
import com.omri.countriesapp.view.callback.CountryClickCallback;
import com.omri.countriesapp.viewmodel.BorderingCountryListViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.omri.countriesapp.view.ui.CountryListFragment.KEY_COUNTRY_NAME;

/**
 * Created by omricohen294 on 05/09/2017.
 */

public class BorderingCountryListFragment extends LifecycleFragment {
    public static final String TAG = "BorderingFragment";

    private CountriesAdapter mCountriesAdapter;
    private FragmentCountryListBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_country_list, container, false);

        mCountriesAdapter = new CountriesAdapter(mCountryClickCallback);
        mBinding.countryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.countryList.setAdapter(mCountriesAdapter);
        mBinding.setIsLoading(true);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BorderingCountryListViewModel.Factory factory = new BorderingCountryListViewModel.Factory(
                getActivity().getApplication(), getArguments().getStringArrayList(KEY_COUNTRY_NAME));

        // Persisting ViewModel through configuration changes etc..
        final BorderingCountryListViewModel viewModel =
                ViewModelProviders.of(this, factory).get(BorderingCountryListViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(BorderingCountryListViewModel viewModel) {
        // Update the list of countries when the data changes
        viewModel.getBorderingCountries().observe(this, new Observer<List<CountryEntity>>() {
            @Override
            public void onChanged(@Nullable List<CountryEntity> countries) {
                if (countries != null && countries.size() >= 1) {
                    // Update UI using the fragment mBinding
                    mCountriesAdapter.setCountryList(countries);
                    mBinding.setIsLoading(false);
                } else {
                    if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        ((MainActivity) getActivity()).showMain();
                    }
                }
            }
        });
    }

    /**
     * Gets a country
     *
     * @param country - country clicked.
     * @return - fragment containing countries bordered by country
     */
    public static BorderingCountryListFragment forCountry(Country country) {
        BorderingCountryListFragment fragment = new BorderingCountryListFragment();
        Bundle args = new Bundle();

        args.putStringArrayList(KEY_COUNTRY_NAME, new ArrayList<>(country.getBorders()));

        fragment.setArguments(args);

        return fragment;
    }

    private final CountryClickCallback mCountryClickCallback = new CountryClickCallback() {

        @Override
        public void onClick(Country country) {
            Log.e(TAG, "@onClick");
        }
    };
}
