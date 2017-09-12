package br.com.rf.dropchallenge.model;

import java.util.ArrayList;
import java.util.List;

public class BeerWrapper {

    private static String TAG = BeerWrapper.class.getSimpleName();

    public List<Beer> beers;

    public BeerWrapper() {
    }

    public BeerWrapper(List<Beer> beers) {
        this.beers = new ArrayList<>(beers);
    }
}
