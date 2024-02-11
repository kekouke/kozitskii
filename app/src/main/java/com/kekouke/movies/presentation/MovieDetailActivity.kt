package com.kekouke.movies.presentation

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

        if (savedInstanceState == null) {
            insertFragment(id)
        }
    }

    private fun insertFragment(id: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.movie_detail_container, MovieDetailFragment.newInstance(id))
            .commit()
    }

    companion object {

        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun newIntent(context: Context, movieId: Int): Intent {
            return Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, movieId)
            }
        }
    }

    override fun onWorkCompleted() {
        finish()
    }
}