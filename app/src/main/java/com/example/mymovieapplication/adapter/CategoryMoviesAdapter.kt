package com.example.mymovieapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovieapplication.adapter.CategoryMoviesAdapter.CategoryMovieRecyclerViewHolder
import com.example.mymovieapplication.databinding.MovieItemBinding
import com.example.mymovieapplication.model.Movie
import com.example.mymovieapplication.model.MovieBase
import com.example.mymovieapplication.utils.Constants
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Abhinav Singh on 09,June,2020
 */
class CategoryMoviesAdapter(
    private val mContext: Context,
    private var moviesList: ArrayList<Movie>?
) : RecyclerView.Adapter<CategoryMovieRecyclerViewHolder>() {
    private var binding: MovieItemBinding? = null
    private var temp: String? = null
    private val ITEM = 0
    private val LOADING = 1
    private var isLoadingAdded = false
    private val genreMap: HashMap<Int, String>
        get() {
            val genreMap = HashMap<Int, String>()
            genreMap[28] = "Action"
            genreMap[12] = "Adventure"
            genreMap[16] = "Animation"
            genreMap[35] = "Comedy"
            genreMap[80] = "Crime"
            genreMap[99] = "Documentary"
            genreMap[18] = "Drama"
            genreMap[10751] = "Family"
            genreMap[14] = "Fantasy"
            genreMap[36] = "History"
            genreMap[27] = "Horror"
            genreMap[10402] = "Music"
            genreMap[9648] = "Mystery"
            genreMap[10749] = "Romance"
            genreMap[878] = "Science Fiction"
            genreMap[53] = "Thriller"
            genreMap[10752] = "War"
            genreMap[37] = "Western"
            genreMap[10770] = "TV Movie"
            return genreMap
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryMovieRecyclerViewHolder {
        val inflater = LayoutInflater.from(mContext)
        binding = MovieItemBinding.inflate(inflater, parent, false)

        return CategoryMovieRecyclerViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: CategoryMovieRecyclerViewHolder, position: Int) {
        holder.binding.movieName.text = moviesList!![position].title
        temp = ""
        for (i in moviesList!![position].genre_ids.indices) {
            temp += if (i == moviesList!![position].genre_ids.size - 1)
                genreMap[moviesList!![position].genre_ids[i]]
            else
                genreMap[moviesList!![position].genre_ids[i]].toString() + " | "
        }
        holder.binding.movieGenre.text = temp
        holder.binding.movieRating.rating = moviesList!![position].vote_average.toFloat() / 2
        val movieYear = moviesList!![position].release_date?.split("-")?.toTypedArray()
        holder.binding.movieYear.text = if (movieYear != null) movieYear[0] else ""
        Glide.with(mContext)
            .load(Constants.ImageBaseURL + moviesList!![position].poster_path)
            .into(holder.binding.movieImage)
        holder.binding.movieItemLayout.clipToOutline = true
    }

    override fun getItemCount(): Int {
        return if (moviesList == null) 0 else moviesList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == moviesList!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    inner class CategoryMovieRecyclerViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

    fun setList(list: ArrayList<Movie>?) {
        list?.let { moviesList?.addAll(it) }
        notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "CategoryMoviesAdapter"
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */
    fun add(r: MovieBase) {
        moviesList?.add(r as Movie)
        notifyItemInserted(moviesList?.size!! - 1)
    }

    fun addAll(moveResults: List<Movie>) {

    }

    fun remove(r: Movie) {
        val position: Int = moviesList?.indexOf(r)!!
        if (position > -1) {
            moviesList?.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(MovieBase(true))
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position: Int = moviesList?.size!! - 1
        val result = getItem(position)
        if (result != null) {
            moviesList?.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Movie {
        return moviesList?.get(position)!!
    }
}