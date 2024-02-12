package com.kekouke.movies.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kekouke.movies.R
import com.kekouke.movies.data.model.Movie
import com.kekouke.movies.presentation.MovieDiffUtilCallback

class MoviesAdapter : ListAdapter<Movie, MoviesAdapter.MovieViewHolder>(MovieDiffUtilCallback()) {

    var onReachEnd: (() ->Unit)? = null
    var onMovieClick: ((Movie) -> Unit)? = null
    var onMovieLongCLick: ((Movie, Boolean) -> Unit)? = null

    private var wasSendOnReachEndEvent = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_item,
            parent,
            false
        )
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        with (holder) {
            tvName.text = movie.name
            tvInformation.text = movie.year
            ivFavourite.isVisible = movie.inFavourite
            Glide.with(itemView)
                .load(movie.posterUrlPreview)
                .placeholder(R.drawable.movie_no_poster)
                .into(ivPreviewPoster)
            itemView.setOnClickListener {
                onMovieClick?.invoke(movie)
            }
            itemView.setOnLongClickListener {
                Glide.with(itemView)
                    .load(movie.posterUrl)
                    .preload()
                onMovieLongCLick?.invoke(movie, movie.inFavourite)
                true
            }
        }
        if (wasReachedEnd(position, 10)) {
            if (!wasSendOnReachEndEvent) {
                onReachEnd?.invoke()
                wasSendOnReachEndEvent = true
            }
        } else {
            wasSendOnReachEndEvent = false
        }
    }

    private fun wasReachedEnd(position: Int, offset: Int): Boolean {
        return position >= itemCount - offset
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvInformation = view.findViewById<TextView>(R.id.tv_information)
        val ivPreviewPoster = view.findViewById<ImageView>(R.id.iv_preview_poster)
        val ivFavourite = view.findViewById<ImageView>(R.id.iv_favourite)
    }
}