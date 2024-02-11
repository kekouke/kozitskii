package com.kekouke.movies.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.kekouke.movies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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

        binding.rvMovies.adapter = moviesAdapter.apply {
            onReachEnd = viewModel::loadMovies
        }

        viewModel.getMovieDetailById()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.movies.observe(this) {
            moviesAdapter.submitList(it)
        }
        viewModel.loading.observe(this) {
            binding.progress.isVisible = it
        }
    }
}