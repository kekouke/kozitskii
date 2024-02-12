package com.kekouke.movies.presentation.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kekouke.movies.R
import com.kekouke.movies.data.model.Movie
import com.kekouke.movies.databinding.FragmentFavouriteMoviesBinding
import com.kekouke.movies.presentation.adapters.MoviesAdapter
import com.kekouke.movies.presentation.moviedetail.MovieDetailActivity
import com.kekouke.movies.presentation.moviedetail.MovieDetailFragment

class FavouriteMoviesFragment : Fragment(), MovieDetailFragment.OnWorkCompletedListener {

    private var _binding: FragmentFavouriteMoviesBinding? = null
    private val binding: FragmentFavouriteMoviesBinding
        get() = _binding ?: throw RuntimeException("FragmentMovieListBinding can't be null")

    private val viewModel by lazy {
        ViewModelProvider(this)[FavouriteMoviesViewModel::class.java]
    }

    private val moviesAdapter = MoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteMoviesBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMovies.adapter = moviesAdapter.apply {
            onMovieLongCLick = viewModel::changeFavouriteState
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
        for (i in 0 until childFragmentManager.backStackEntryCount) {
            childFragmentManager.popBackStack()
        }
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) {
            moviesAdapter.submitList(it)
            binding.progress.visibility = View.GONE
        }
    }

    private fun isLandscapeMode() = binding.movieDetailContainer != null

    private fun launchMovieDetailActivity(movie: Movie) {
        val activity = requireActivity()
        activity.startActivity(
            MovieDetailActivity.newIntent(activity, movie.id, MovieDetailActivity.MODE_FAVOURITE)
        )
    }

    private fun launchMovieDetailFragment(movie: Movie) {
        childFragmentManager.popBackStack()
        childFragmentManager.beginTransaction()
            .replace(
                R.id.movie_detail_container,
                MovieDetailFragment.newInstance(movie.id, MovieDetailActivity.MODE_FAVOURITE)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavouriteMoviesFragment()
    }

    override fun onWorkCompleted() {
        childFragmentManager.popBackStack()
    }
}