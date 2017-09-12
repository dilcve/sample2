package br.com.rf.dropchallenge.source;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.rf.dropchallenge.model.Beer;
import br.com.rf.dropchallenge.model.BeerWrapper;

public interface BeersDataSource {

    void saveBeers(int type, @NonNull BeerWrapper beerWrapper);

    void saveBeers(int type, @NonNull List<Beer> beers);

    void deleteAllBeers(int type);

    void getBeers(int type, @NonNull LoadBeersCallback callback);

    void getMoreBeers(int type, @NonNull LoadBeersCallback callback);

    interface LoadBeersCallback {

        void onBeersLoaded(List<Beer> beers);

        void onDataNotAvailable();
    }


}
