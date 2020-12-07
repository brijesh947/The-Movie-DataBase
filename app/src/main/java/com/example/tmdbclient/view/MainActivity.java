package com.example.tmdbclient.view;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tmdbclient.R;
import com.example.tmdbclient.adapter.MovieAdapter;
import com.example.tmdbclient.model.Movie;
import com.example.tmdbclient.model.MovieDBResponse;
import com.example.tmdbclient.service.MovieDataService;
import com.example.tmdbclient.service.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Observable<MovieDBResponse> movieDBResponseObservable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("TMDB Popular Movies Today");

        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("MainActivity", "onRefresh: swipe Refresh layout");
                getPopularMoviesWithRx();
            }
        });
        getPopularMoviesWithRx();
    }

    private void getPopularMoviesWithRx() {
        MovieDataService movieDataService = RetrofitInstance.getService();
//        Observable<MovieDBResponse> observable = movieDataService.getPopularMoviesWithRx(this.getString(R.string.api_key));
         movieDBResponseObservable = movieDataService.getPopularMoviesWithRx(this.getString(R.string.api_key));
         compositeDisposable.add( movieDBResponseObservable.subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .flatMap(new Function<MovieDBResponse, Observable<Movie>>() {
                     @Override
                     public Observable<Movie> apply(MovieDBResponse movieDBResponse) throws Exception {
                         return Observable.fromArray(movieDBResponse.getResults().toArray(new Movie[0]));
                     }
                 })
                 .filter(new Predicate<Movie>() {
                     @Override
                     public boolean test(Movie movie) throws Exception {
                         return movie.getVoteAverage()>7.0;
                     }
                 })
                 .subscribeWith(new DisposableObserver<Movie>() {
                     @Override
                     public void onNext(Movie movie) {
                         movies.add(movie);
                     }

                     @Override
                     public void onError(Throwable e) {

                     }

                     @Override
                     public void onComplete() {
                     showOnRecyclerView();
                     }
                 }));


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
//        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        compositeDisposable.clear();
    }
}