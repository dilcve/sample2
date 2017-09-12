package br.com.rf.dropchallenge.source;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import br.com.rf.dropchallenge.model.Beer;
import br.com.rf.dropchallenge.model.BeerWrapper;
import br.com.rf.dropchallenge.source.local.BeersLocalDataSource;
import br.com.rf.dropchallenge.source.remote.BeersRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class BeersRepository implements BeersDataSource {

    private static BeersRepository INSTANCE = null;

    private final BeersDataSource mBeersRemoteDataSource;
    private final BeersDataSource mBeersLocalDataSource;

    // Prevent direct instantiation.
    private BeersRepository(@NonNull BeersDataSource usersRemoteDataSource, @NonNull BeersDataSource usersLocalDataSource) {
        mBeersRemoteDataSource = checkNotNull(usersRemoteDataSource);
        mBeersLocalDataSource = checkNotNull(usersLocalDataSource);
    }

    public static BeersRepository getInstance(BeersDataSource usersRemoteDataSource, BeersDataSource usersLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BeersRepository(usersRemoteDataSource, usersLocalDataSource);
        }
        return INSTANCE;
    }

    public static BeersRepository provideBeersRepository(@NonNull Context context) {
        checkNotNull(context);
        return BeersRepository.getInstance(BeersRemoteDataSource.getInstance(),
                BeersLocalDataSource.getInstance(context));
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getBeers(int type, @NonNull final LoadBeersCallback callback) {
        checkNotNull(callback);
        getBeersFromLocalDataSource(type, callback);
    }

    @Override
    public void getMoreBeers(int type, @NonNull LoadBeersCallback callback) {
        checkNotNull(callback);
        getBeersFromRemoteDataSource(type, callback);
    }

    @Override
    public void saveBeers(int type, @NonNull BeerWrapper beerWrapper) {
        mBeersLocalDataSource.saveBeers(type, beerWrapper);
    }

    @Override
    public void saveBeers(int type, @NonNull List<Beer> beers) {
        mBeersLocalDataSource.saveBeers(type, beers);
    }

    @Override
    public void deleteAllBeers(int type) {
        mBeersLocalDataSource.deleteAllBeers(type);
    }

    private void getBeersFromRemoteDataSource(final int type, @NonNull final LoadBeersCallback callback) {
        mBeersRemoteDataSource.getBeers(type, new LoadBeersCallback() {
            @Override
            public void onBeersLoaded(List<Beer> beers) {
                saveBeers(type, beers);
                callback.onBeersLoaded(beers);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getBeersFromLocalDataSource(final int type, @NonNull final LoadBeersCallback callback) {
        mBeersLocalDataSource.getBeers(type, new LoadBeersCallback() {
            @Override
            public void onBeersLoaded(List<Beer> beers) {
                callback.onBeersLoaded(beers);
            }

            @Override
            public void onDataNotAvailable() {
                getBeersFromRemoteDataSource(type, callback);
            }
        });
    }
}
