package com.kekouke.movies.presentation.moviedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kekouke.movies.R
import com.kekouke.movies.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : AppCompatActivity(), MovieDetailFragment.OnWorkCompletedListener {

    private val binding by lazy {
        ActivityMovieDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val id = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        val mode = intent.getIntExtra(EXTRA_MODE, -1)

        if (savedInstanceState == null) {
            insertFragment(id, mode)
        }
    }

    private fun insertFragment(id: Int, mode: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.movie_detail_container, MovieDetailFragment.newInstance(id, mode))
            .commit()
    }

    companion object {

        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        private const val EXTRA_MODE = "EXTRA_MODE"

        const val MODE_FAVOURITE = 100
        const val MODE_POPULAR = 101

        fun newIntent(context: Context, movieId: Int, mode: Int): Intent {
            return Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, movieId)
                putExtra(EXTRA_MODE, mode)
            }
        }
    }

    override fun onWorkCompleted() {
        finish()
    }
}