package com.kekouke.movies.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.kekouke.movies.R
import com.kekouke.movies.data.Movie
import com.kekouke.movies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MovieDetailFragment.OnWorkCompletedListener {

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val moviesAdapter = MoviesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.layoutNetworkError.btnRetry.setOnClickListener {
            viewModel.loadMovies()
        }

        binding.rvMovies.adapter = moviesAdapter.apply {
            onReachEnd = viewModel::loadMovies
            onMovieClick = {
                if (isLandscapeMode()) {
                    launchMovieDetailFragment(it)
                } else {
                    launchMovieDetailActivity(it)
                }
            }
        }

        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    private fun isLandscapeMode() = binding.movieDetailContainer != null

    private fun launchMovieDetailActivity(movie: Movie) {
        startActivity(MovieDetailActivity.newIntent(this, movie.id))
    }

    private fun launchMovieDetailFragment(movie: Movie) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.movie_detail_container, MovieDetailFragment.newInstance(movie.id))
            .addToBackStack(null)
            .commit()
    }

    private fun observeViewModel() {
        viewModel.movies.observe(this) {
            moviesAdapter.submitList(it)
        }
        viewModel.loading.observe(this) {
            binding.progress.isVisible = it
        }
        viewModel.error.observe(this) {
            binding.layoutNetworkError.root.isVisible = it
            if (moviesAdapter.itemCount > 0 && it) {
                binding.layoutNetworkError.root.isVisible = false
                Toast.makeText(
                    this,
                    getString(R.string.network_error_message_short),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onWorkCompleted() {
        onBackPressedDispatcher.onBackPressed()
    }
}