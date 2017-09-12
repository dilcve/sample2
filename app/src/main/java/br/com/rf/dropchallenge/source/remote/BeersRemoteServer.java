package br.com.rf.dropchallenge.source.remote;

import java.util.List;

import br.com.rf.dropchallenge.model.Beer;
import retrofit2.Call;
import retrofit2.http.GET;


public interface BeersRemoteServer {

    public static final String USER_BASE_API = "http://api.punkapi.com";

    @GET("/v2/beers?abv_lt=4&per_page=50")
    Call<List<Beer>> getLightBeers();

    @GET("/v2/beers?abv_lt=7&abv_gt=4&per_page=50")
    Call<List<Beer>> getLMediumBeers();

    @GET("/v2/beers?&abv_gt=7&per_page=50")
    Call<List<Beer>> getStrongBeers();

    @GET("/v2/beers/random")
    Call<List<Beer>> getRandomBeers();
}
