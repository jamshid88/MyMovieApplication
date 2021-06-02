package com.example.mymovieapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapplication.adapter.CategoryMoviesAdapter
import com.example.mymovieapplication.databinding.SearchMoviesBinding
import com.example.mymovieapplication.model.Movie
import com.example.mymovieapplication.ui.screen.pagination.PaginationScrollListener
import com.example.mymovieapplication.utils.Constants
import com.example.mymovieapplication.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Abhinav Singh on 09,June,2020
 */
@AndroidEntryPoint
class SearchMovies : Fragment() {
    private var binding: SearchMoviesBinding? = null
    private var viewModel: HomeViewModel? = null
    private var queryMap: HashMap<String?, String?>? = null
    private var adapter: CategoryMoviesAdapter? = null
    private val moviesList: ArrayList<Movie>? = ArrayList()
    private val PAGE_START = 1
    private var mIsLoading = false
    private var mIsLastPage = false

    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private val TOTAL_PAGES = 100
    private var currentPage = PAGE_START
    private var queryText = "news"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchMoviesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

//    private val controller = MovieItemController {
//        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.searchKeyword.setText(queryText)
        queryMap = HashMap()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        queryMap!!["api_key"] = Constants.API_KEY
        queryMap!!["query"] = queryText
        initRecyclerView()
        observeData()
        viewModel!!.getQueriedMovies(queryMap)
        binding!!.searchMovie.setOnClickListener {
            fetchData()
        }
        binding!!.searchKeyword.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                queryText = binding!!.searchKeyword.text.toString().trim { it <= ' ' }.toLowerCase()
                queryMap!!.clear()
                queryMap!!["api_key"] = Constants.API_KEY
                queryMap!!["query"] = queryText
                viewModel!!.getQueriedMovies(queryMap)
            }
            false
        }
    }

    private fun fetchData() {
        queryText = binding!!.searchKeyword.text.toString().trim { it <= ' ' }.toLowerCase()
        queryMap!!.clear()
        if (currentPage <= TOTAL_PAGES)
            adapter?.addLoadingFooter()
        else
            mIsLastPage = true

        queryMap!!["api_key"] = Constants.API_KEY
        queryMap!!["query"] = queryText
        queryMap!!["page"] = currentPage.toString()
        viewModel!!.getQueriedMovies(queryMap)
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding!!.searchMoviesRecyclerView.layoutManager = linearLayoutManager
        adapter = CategoryMoviesAdapter(requireContext(), moviesList)
        binding!!.searchMoviesRecyclerView.adapter = adapter
        binding!!.searchMoviesRecyclerView.addOnScrollListener(object: PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                currentPage += 1
                fetchData()
                mIsLoading = true
            }

            override val totalPageCount: Int
                get() = TOTAL_PAGES
            override val isLastPage: Boolean
                get() = mIsLastPage
            override val isLoading: Boolean
                get() = mIsLoading
        })
    }

    private fun observeData() {
        viewModel!!.queriesMovies.observe(
            viewLifecycleOwner, {
                if (it.isEmpty()) {
                    mIsLastPage = true
                } else {
                    adapter!!.setList(it)
                }
                mIsLoading = false
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}