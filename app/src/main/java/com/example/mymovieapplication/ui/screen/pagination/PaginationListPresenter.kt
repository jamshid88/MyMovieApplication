package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.sample.domain.Movie
import ru.surfstudio.android.easyadapter.sample.interactor.FirstDataRepository
import ru.surfstudio.android.easyadapter.sample.network.NetworkModule
import ru.surfstudio.android.easyadapter.sample.ui.utils.Constants
import java.util.*
import javax.inject.Inject

@PerScreen
internal class PaginationListPresenter @Inject constructor(
    basePresenterDependency: BasePresenterDependency,
    private val repository: FirstDataRepository
) : BasePresenter<PaginationListActivityView>(basePresenterDependency) {

    private val sm: PaginationListScreenModel = PaginationListScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            loadData()
        } else {
            view.render(sm)
        }
    }

    private val disposables = CompositeDisposable()
    val queriesMovies = MutableLiveData<ArrayList<Movie>>()
    private var queryMap: HashMap<String, String> = HashMap<String, String>()
    private fun loadData() {
        queryMap.clear()
        queryMap["api_key"] = Constants.API_KEY
        queryMap["query"] = "news"
        queryMap["page"] = (sm.pageList.nextPage+1).toString()
        disposables.add(
            NetworkModule.provideMovieApiService().getMoviesBySearch(queryMap)
                ?.subscribeOn(Schedulers.io())
                ?.map<ArrayList<Movie>> { jsonObject ->
                    val jsonArray = jsonObject?.getAsJsonArray("results")
                    Gson().fromJson(
                        jsonArray.toString(),
                        object : TypeToken<DataList<Movie?>?>() {}.type
                    )
                }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result: ArrayList<Movie> ->
                        with(sm) {
                            val dataList = DataList(
                                result,
                                sm.pageList.nextPage ,
                                FirstDataRepository.PAGES_COUNT,
                                FirstDataRepository.PAGE_SIZE
                            )
                            pageList.merge(dataList)
                            sm.setNormalPaginationState(pageList.canGetMore())
                        }

                        view.render(sm)
                    }
                ) { error: Throwable ->
                    Log.e("TAG", "getPopularMovies: " + error.message)
                    sm.setErrorPaginationState()
                    view.render(sm)
                }
        )
    }

    // error demonstration
    private fun getDelay() =
        if (sm.pageList.nextPage > FirstDataRepository.ERROR_PAGE_NUMBER) {
            Long.MAX_VALUE
        } else {
            500L
        }

    fun loadMore() = loadData()

    private fun getDataByPage(): Observable<DataList<Movie>> {
        return repository.getDataByPage(sm.pageList.nextPage)
    }

}