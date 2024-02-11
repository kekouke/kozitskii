package com.kekouke.movies.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kekouke.movies.R
import com.kekouke.movies.data.Country
import com.kekouke.movies.data.Genre
import com.kekouke.movies.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMovieDetailBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MovieDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val id = intent.getIntExtra(EXTRA_MOVIE_ID, -1)

        observeViewModel()

        if (savedInstanceState == null) {
            viewModel.loadMovieDetail(id)
        }
    }

    private fun observeViewModel() {
        viewModel.movieDetail.observe(this) {
            with (binding) {
                Glide.with(this@MovieDetailActivity)
                    .load(it.posterUrl)
                    .into(ivPoster)
                tvDescription.text = it.description
                tvName.text = it.name
                ivBack.setOnClickListener {
                    finish()
                }
                setupGenresLabel(tvGenres, it.genres)
                setupCountryLabel(tvCountry, it.countries)
            }
        }
    }

    private fun setupCountryLabel(tvCountry: TextView, countries: List<Country>) {
        val firstCountry = countries.firstOrNull()
        tvCountry.text = getString(R.string.label_country, firstCountry?.toString() ?: "")
    }

    private fun setupGenresLabel(tvGenres: TextView, genres: List<Genre>) {
        var genresAsString = StringBuilder("")
        var isFirst = true
        for (genre in genres) {
            if (isFirst) {
                genresAsString.append(genre)
                isFirst = false
                continue
            }
            genresAsString.append(", $genre")
        }
        tvGenres.text = getString(R.string.label_genres, genresAsString)
    }

    companion object {

        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun newIntent(context: Context, movieId: Int): Intent {
            return Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, movieId)
            }
        }
    }
}