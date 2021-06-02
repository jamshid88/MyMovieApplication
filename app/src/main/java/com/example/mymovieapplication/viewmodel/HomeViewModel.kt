package com.example.mymovieapplication.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovieapplication.model.Actor
import com.example.mymovieapplication.model.Cast
import com.example.mymovieapplication.model.Movie
import com.example.mymovieapplication.model.MovieResponse
import com.example.mymovieapplication.repository.Repository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by Abhinav Singh on 09,June,2020
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val currentlyShowingList = MutableLiveData<ArrayList<Movie>?>()
    val popularMoviesList = MutableLiveData<ArrayList<Movie>>()
    val topRatedMoviesList = MutableLiveData<ArrayList<Movie>>()
    val upcomingMoviesList = MutableLiveData<ArrayList<Movie>>()
    val queriesMovies = MutableLiveData<ArrayList<Movie>>()
    val movieCastList = MutableLiveData<ArrayList<Cast>>()
    val movie = MutableLiveData<Movie>()
    val actor = MutableLiveData<Actor>()
    private val disposables = CompositeDisposable()
    fun getCurrentlyShowingMovies(map: HashMap<String?, String?>?) {
        disposables.add(
            repository.getCurrentlyShowing(map)
                ?.subscribeOn(Schedulers.io())
                ?.map<ArrayList<Movie>> { it?.results }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<ArrayList<Movie>>() {
                    override fun onNext(@NonNull t: @NonNull ArrayList<Movie>?) {
                        currentlyShowingList.value = t
                    }

                    override fun onError(e: @NonNull Throwable?) {}
                    override fun onComplete() {}
                })
        )
    }

    fun getPopularMovies(map: HashMap<String?, String?>?) {
        disposables.add(
            repository.getPopular(map)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    Consumer { result: MovieResponse -> popularMoviesList.setValue(result.results) } as (MovieResponse?) -> Unit,
                    { error: Throwable ->
                        Log.e(
                            TAG,
                            "getPopularMovies: " + error.message
                        )
                    })
        )
    }

    fun getTopRatedMovies(map: HashMap<String?, String?>?) {
        disposables.add(
            repository.getTopRated(map)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    Consumer { result: MovieResponse -> topRatedMoviesList.setValue(result.results) } as (MovieResponse?) -> Unit,
                    { error: Throwable -> Log.e(TAG, "getTopRated: " + error.message) })
        )
    }

    fun getUpcomingMovies(map: HashMap<String?, String?>?) {
        disposables.add(
            repository.getUpcoming(map)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    Consumer { result: MovieResponse -> upcomingMoviesList.value =
                        result.results } as (MovieResponse?) -> Unit,
                    { error: Throwable -> Log.e(TAG, "getUpcoming: " + error.message) })
        )
    }

    fun getMovieDetails(movieId: Int, map: HashMap<String?, String?>?) {
        disposables.add(
            repository.getMovieDetails(movieId, map)
                ?.subscribeOn(Schedulers.io())
                ?.map { movie ->
                    val genreNames = ArrayList<String>()
                    // MovieResponse gives list of genre(object) so we will map each id to it genre name here.a
                    for (genre in movie!!.genres) {
                        genreNames.add(genre.name)
                    }
                    movie.genre_names = genreNames
                    movie
                }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result: Movie -> movie.setValue(result) }
                ) { error: Throwable -> Log.e(TAG, "getMovieDetails: " + error.message) }
        )
    }

    fun getCast(movieId: Int, map: HashMap<String?, String?>?) {
        disposables.add(
            repository.getCast(movieId, map)
                ?.subscribeOn(Schedulers.io())
                ?.map<ArrayList<Cast>> { jsonObject ->
                    val jsonArray = jsonObject?.getAsJsonArray("cast")
                    Gson().fromJson(
                        jsonArray.toString(),
                        object : TypeToken<ArrayList<Cast?>?>() {}.type
                    )
                }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result: ArrayList<Cast> -> movieCastList.setValue(result) }
                ) { error: Throwable -> Log.e(TAG, "getCastList: " + error.message) }
        )
    }

    fun getActorDetails(personId: Int, map: HashMap<String?, String?>?) {
        disposables.add(
            repository.getActorDetails(personId, map)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    Consumer { result: Actor -> actor.value = result } as (Actor?) -> Unit,
                    { error: Throwable ->
                        Log.e(
                            TAG,
                            "getActorDetails: " + error.message
                        )
                    })
        )
    }

    fun getQueriedMovies(map: HashMap<String?, String?>?) {
        disposables.add(
            repository.getMoviesBySearch(map)
                ?.subscribeOn(Schedulers.io())
                ?.map<ArrayList<Movie>> { jsonObject ->
                    val jsonArray = jsonObject?.getAsJsonArray("results")
                    Gson().fromJson(
                        jsonArray.toString(),
                        object : TypeToken<ArrayList<Movie?>?>() {}.type
                    )
                }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result: ArrayList<Movie> -> queriesMovies.setValue(result) }
                ) { error: Throwable -> Log.e(TAG, "getPopularMovies: " + error.message) }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}