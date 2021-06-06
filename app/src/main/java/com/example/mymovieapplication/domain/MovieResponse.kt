package ru.surfstudio.android.easyadapter.sample.domain

import ru.surfstudio.android.easyadapter.sample.domain.Movie
import java.util.*

class MovieResponse(
    var page: Int,
    var total_pages: Int,
    var total_results: Int,
    var results: ArrayList<Movie>
)