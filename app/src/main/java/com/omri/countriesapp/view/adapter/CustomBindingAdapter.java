package com.omri.countriesapp.view.adapter;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * Created by omricohen294 on 05/09/2017.
 */

public class CustomBindingAdapter {

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
