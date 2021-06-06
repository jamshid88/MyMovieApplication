package ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers

import android.os.Build
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import ru.surfstudio.android.easyadapter.sample.domain.Movie
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.ui.utils.Constants
import java.util.*

class MovieItemController(
    private val onClickListener: (Movie) -> Unit
) : BindableItemController<Movie, MovieItemController.Holder>() {

    private lateinit var temp: String
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

    override fun getItemId(data: Movie): String = data.hashCode().toString()

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(parent)

    inner class Holder(parent: ViewGroup?) :
        BindableViewHolder<Movie>(parent, R.layout.movie_item) {

        private lateinit var data: Movie
        private val tvMovieName: TextView = itemView.findViewById(R.id.movieName)
        private val tvMovieGenre: TextView = itemView.findViewById(R.id.movieGenre)
        private val movieRating: RatingBar = itemView.findViewById(R.id.movieRating)
        private val tvMovieYear: TextView = itemView.findViewById(R.id.movieYear)
        private val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
        private val movieItemLayout: RelativeLayout = itemView.findViewById(R.id.movieItemLayout)

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun bind(data: Movie) {
            val mContext = tvMovieName.context
            this.data = data
            tvMovieName.text = data.title
            temp = ""
            for (i in data.genre_ids.indices) {
                temp += if (i == data.genre_ids.size - 1) genreMap[data.genre_ids[i]] else genreMap[data.genre_ids[i]].toString() + " | "
            }
            tvMovieGenre.text = temp
            movieRating.rating = data.vote_average.toFloat() / 2
            val movieYear = data.release_date?.split("-")?.toTypedArray()
            tvMovieYear.text = if (movieYear != null) movieYear[0] else ""
            Glide.with(mContext).load(Constants.ImageBaseURL + data.poster_path).into(movieImage)
            movieItemLayout.clipToOutline = true
        }
    }

    companion object {
        private const val TAG = "CategoryMoviesAdapter"
    }
}