package com.omri.countriesapp.view.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.omri.countriesapp.R;
import com.omri.countriesapp.model.Country;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add countries list fragment if this is first creation
        if (savedInstanceState == null) {
            CountryListFragment fragment = new CountryListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, CountryListFragment.TAG).commit();
        }
    }

    /**
     * Creates a fragment containing countries bordered by "country"
     * @param country country received by CountryListFragment.@onClick
     */
    public void show(Country country) {
        BorderingCountryListFragment borderingFragment = BorderingCountryListFragment.forCountry(country);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("main_fragment")
                .replace(R.id.fragment_container,
                        borderingFragment, null).commit();
    }

    // Returns to main screen
    public void showMain(){
        getSupportFragmentManager().popBackStack();
    }
}
