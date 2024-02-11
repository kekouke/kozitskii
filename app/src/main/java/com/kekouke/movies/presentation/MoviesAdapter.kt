package com.kekouke.movies.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kekouke.movies.R
import com.kekouke.movies.data.Movie

class MoviesAdapter : ListAdapter<Movie, MoviesAdapter.MovieViewHolder>(MovieDiffUtilCallback()) {

    var onReachEnd: (() ->Unit)? = null

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
            Glide.with(itemView)
                .load(movie.posterUrlPreview)
                .into(ivPreviewPoster)
        }
        if (wasReachedEnd(position, 10)) {
            onReachEnd?.invoke()
        }
    }

    private fun wasReachedEnd(position: Int, offset: Int): Boolean {
        return position >= itemCount - offset
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvInformation = view.findViewById<TextView>(R.id.tv_information)
        val ivPreviewPoster = view.findViewById<ImageView>(R.id.iv_preview_poster)
    }
}