package com.omri.countriesapp.model;

import java.util.List;

/**
 * Created by omricohen294 on 05/09/2017.
 */

public interface Country {
    String getName();

    String getNativeName();

    String getAlpha3Code();

    List<String> getBorders();
}
