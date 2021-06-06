package ru.surfstudio.android.easyadapter.sample.interactor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import ru.surfstudio.android.easyadapter.sample.domain.Movie
import ru.surfstudio.android.easyadapter.sample.network.MovieApiService
import ru.surfstudio.android.easyadapter.sample.network.NetworkModule
import ru.surfstudio.android.easyadapter.sample.ui.utils.Constants
import java.util.HashMap
import javax.inject.Inject

// aliases for different DataList distinguishing. In real project only one will be used
typealias DataListPageCount<T> = DataList<T>
typealias DataListLimitOffset<T> = ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList<T>

class FirstDataRepository @Inject constructor() {

    private val list = ArrayList<Movie>()
    private val disposables = CompositeDisposable()
    val queriesMovies = MutableLiveData<java.util.ArrayList<Movie>>()
    companion object {
        private const val DATA_SIZE = 150
        const val PAGE_SIZE = 20

        const val PAGES_COUNT = DATA_SIZE / PAGE_SIZE

        // page number which will be used for PaginationState.ERROR setting
        // in order to demonstrate adapter's footer
        const val ERROR_PAGE_NUMBER = PAGES_COUNT / 2
    }

//    init {
//        (1..DATA_SIZE + 1).forEach { list.add(Movie(it)) }

//        val rxAdapter = RxJava3CallAdapterFactory.create()
//        retrofit = Retrofit.Builder().baseUrl("")
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(httpClient)
//            .addCallAdapterFactory(rxAdapter).build()
//
//    }

    fun getQueriedMovies(map: HashMap<String, String>) {
        disposables.add(
            NetworkModule.provideMovieApiService().getMoviesBySearch(map)
                ?.subscribeOn(Schedulers.io())
                ?.map<java.util.ArrayList<Movie>> { jsonObject ->
                    val jsonArray = jsonObject?.getAsJsonArray("results")
                    Gson().fromJson(
                        jsonArray.toString(),
                        object : TypeToken<java.util.ArrayList<Movie?>?>() {}.type
                    )
                }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result: java.util.ArrayList<Movie> ->
                        queriesMovies.setValue(result)
                    }
                ) { error: Throwable -> Log.e("TAG", "getPopularMovies: " + error.message) }
        )
    }

    /**
     * Load data for page number
     */
    fun getDataByPage(page: Int): Observable<DataListPageCount<Movie>> {
        val startIndex = PAGE_SIZE * (page - 1)



        return Observable.just(
                DataList(
//                    NetworkModule.provideMovieApiService().getMoviesBySearch()
                        list.subList(startIndex, startIndex + PAGE_SIZE),
                        page,
                        PAGES_COUNT,
                        PAGE_SIZE


                )
        )


    }


    /**
     * Load data for offset
     */
    fun getDataByOffset(offset: Int, limit: Int = 15) =
            Observable.just(DataListLimitOffset(
                    list.subList(offset, offset + limit),
                    limit,
                    offset,
                    DATA_SIZE)
            )
}