package com.kekouke.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kekouke.movies.data.Movie

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    var movies: List<Movie> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_item,
            parent,
            false
        )
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        with (holder) {
            tvName.text = movie.name
            tvInformation.text = movie.year
            Glide.with(itemView)
                .load(movie.posterUrlPreview)
                .into(ivPreviewPoster)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvInformation = view.findViewById<TextView>(R.id.tv_information)
        val ivPreviewPoster = view.findViewById<ImageView>(R.id.iv_preview_poster)
    }
}