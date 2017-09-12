package br.com.rf.dropchallenge.source.remote;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.rf.dropchallenge.model.Beer;
import br.com.rf.dropchallenge.model.BeerWrapper;
import br.com.rf.dropchallenge.source.BeersDataSource;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class BeersRemoteDataSource implements BeersDataSource {

    private static BeersRemoteDataSource INSTANCE;

    public static BeersRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeersRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private BeersRemoteDataSource() {
    }

    @Override
    public void getBeers(final int type, @NonNull final LoadBeersCallback callback) {
        checkNotNull(callback);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.interceptors().add(logging);  //---> debug


        client.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BeersRemoteServer.USER_BASE_API)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final BeersRemoteServer beersService = retrofit.create(BeersRemoteServer.class);
        Call<List<Beer>> call = null;

        switch (type) {
            case Beer.TYPE_LIGHT:
                call = beersService.getLightBeers();
                break;
            case Beer.TYPE_MEDIUM:
                call = beersService.getLMediumBeers();
                break;
            case Beer.TYPE_STRONG:
                call = beersService.getStrongBeers();
                break;
            case Beer.TYPE_RANDOM:
                call = beersService.getRandomBeers();
                break;
        }

        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> beerResponse, Response<List<Beer>> response) {
                List<Beer> beers = response.body();
                if (beers != null) {
                    callback.onBeersLoaded(beers);
                } else {
                    callback.onDataNotAvailable();
                }

            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void getMoreBeers(int type, @NonNull LoadBeersCallback callback) {
        getBeers(type, callback);
    }

    @Override
    public void saveBeers(int type, @NonNull BeerWrapper beerWrapper) {
        //nothing to do on remote
    }

    @Override
    public void saveBeers(int type, @NonNull List<Beer> beer) {
        //nothing to do on remote
    }

    @Override
    public void deleteAllBeers(int type) {
        //nothing to do on remote
    }

}
