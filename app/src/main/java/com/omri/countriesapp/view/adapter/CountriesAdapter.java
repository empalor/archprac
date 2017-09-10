package com.omri.countriesapp.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.omri.countriesapp.R;
import com.omri.countriesapp.databinding.CountryListItemBinding;
import com.omri.countriesapp.db.entity.CountryEntity;
import com.omri.countriesapp.model.Country;
import com.omri.countriesapp.view.callback.CountryClickCallback;

import java.util.List;

/**
 * Created by omricohen294 on 05/09/2017.
 */

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder> {

    List<? extends CountryEntity> countryList;

    @Nullable
    private final CountryClickCallback countryClickCallback;

    public CountriesAdapter(@Nullable CountryClickCallback countryClickCallback) {
        this.countryClickCallback = countryClickCallback;
    }

    public void setCountryList(final List<? extends CountryEntity> countryList) {
        if (this.countryList == null) {
            this.countryList = countryList;
            notifyItemRangeInserted(0, countryList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return CountriesAdapter.this.countryList.size();
                }

                @Override
                public int getNewListSize() {
                    return countryList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return CountriesAdapter.this.countryList.get(oldItemPosition).getName().equals(
                            countryList.get(newItemPosition).getName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Country country = countryList.get(newItemPosition);
                    Country old = countryList.get(oldItemPosition);
                    return country.getName().equals(old.getName());
                }
            });
            this.countryList = countryList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public CountriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CountryListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.country_list_item,
                        parent, false);

        binding.setCallback(countryClickCallback);

        return new CountriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CountriesViewHolder holder, int position) {
        holder.binding.setCountry(countryList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return countryList == null ? 0 : countryList.size();
    }

    static class CountriesViewHolder extends RecyclerView.ViewHolder {

        final CountryListItemBinding binding;

        public CountriesViewHolder(CountryListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
