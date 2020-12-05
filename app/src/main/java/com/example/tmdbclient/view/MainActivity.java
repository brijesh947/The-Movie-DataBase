package com.example.tmdbclient.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.tmdbclient.R;
import com.example.tmdbclient.adapter.MovieAdapter;
import com.example.tmdbclient.model.Movie;
import com.example.tmdbclient.model.MovieDBResponse;
import com.example.tmdbclient.service.MovieDataService;
import com.example.tmdbclient.service.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Movie> movies;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("TMDB Popular Movies Today");
        getPopularMovies();
        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularMovies();
            }
        });
    }

    private void getPopularMovies() {
        MovieDataService movieDataService = RetrofitInstance.getService();
        Call<MovieDBResponse> call= movieDataService.getPopularMovies(this.getString(R.string.api_key));
        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                MovieDBResponse movieDBResponse = response.body();
                if(movieDBResponse!=null && movieDBResponse.getResults()!=null){
                    movies = (ArrayList<Movie>) movieDBResponse.getResults();
                    showOnRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {

            }
        });
    }

    private void showOnRecyclerView() {
        recyclerView = findViewById(R.id.rvMovies);
        movieAdapter = new MovieAdapter(MainActivity.this,movies);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setEnabled(false);
    }
}