package ru.surfstudio.android.easyadapter.sample.interactor

import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import ru.surfstudio.android.easyadapter.sample.domain.Actor
import ru.surfstudio.android.easyadapter.sample.domain.Movie
import ru.surfstudio.android.easyadapter.sample.domain.MovieResponse
import ru.surfstudio.android.easyadapter.sample.network.MovieApiService
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(var apiService: MovieApiService) {
    fun getCurrentlyShowing(map: HashMap<String?, String?>?): Observable<MovieResponse?>? {
        return apiService.getCurrentlyShowing(map)
    }

    fun getPopular(map: HashMap<String?, String?>?): Observable<MovieResponse?>? {
        return apiService.getPopular(map)
    }

    fun getTopRated(map: HashMap<String?, String?>?): Observable<MovieResponse?>? {
        return apiService.getTopRated(map)
    }

    fun getUpcoming(map: HashMap<String?, String?>?): Observable<MovieResponse?>? {
        return apiService.getUpcoming(map)
    }

    fun getMovieDetails(movieId: Int, map: HashMap<String?, String?>?): Observable<Movie?>? {
        return apiService.getMovieDetails(movieId, map)
    }

    fun getCast(movieId: Int, map: HashMap<String?, String?>?): Observable<JsonObject?>? {
        return apiService.getCast(movieId, map)
    }

    fun getActorDetails(personId: Int, map: HashMap<String?, String?>?): Observable<Actor?>? {
        return apiService.getActorDetails(personId, map)
    }

    fun getMoviesBySearch(map: HashMap<String, String>): Observable<JsonObject?>? {
        return apiService.getMoviesBySearch(map)
    }

    companion object {
        private const val TAG = "Repository"
    }
}