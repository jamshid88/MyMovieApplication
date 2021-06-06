package ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import ru.surfstudio.android.easyadapter.sample.domain.Movie

class FirstDataItemController(
        private val onClickListener: (Movie) -> Unit
) : BindableItemController<Movie, FirstDataItemController.Holder>() {

    override fun getItemId(data: Movie): String = data.hashCode().toString()

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(parent)

    inner class Holder(
            parent: ViewGroup?
    ) : BindableViewHolder<Movie>(parent, R.layout.first_data_item_controller) {

        private lateinit var data: Movie
        private val firstTv: TextView = itemView.findViewById(R.id.first_tv)

        init {
            itemView.findViewById<RelativeLayout>(R.id.first_data_container).apply {
                setOnClickListener { onClickListener(data) }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(data: Movie) {
            this.data = data
            firstTv.text = "$data click me!"
        }
    }
}