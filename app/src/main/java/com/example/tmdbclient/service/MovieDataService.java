package com.example.tmdbclient.service;

import com.example.tmdbclient.model.MovieDBResponse;

import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MovieDataService {
//    @GET("movie/popular")
//    Call<MovieDBResponse> getPopularMovies(@Query("api_key") String apiKey);


    @GET("movie/popular")
    Observable<MovieDBResponse> getPopularMoviesWithRx(@Query("api_key") String apiKey);
}
