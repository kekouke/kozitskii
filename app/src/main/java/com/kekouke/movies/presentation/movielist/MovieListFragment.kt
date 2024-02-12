package com.kekouke.movies.presentation.movielist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kekouke.movies.R
import com.kekouke.movies.data.model.Movie
import com.kekouke.movies.databinding.FragmentMovieListBinding
import com.kekouke.movies.presentation.adapters.MoviesAdapter
import com.kekouke.movies.presentation.moviedetail.MovieDetailActivity
import com.kekouke.movies.presentation.moviedetail.MovieDetailFragment

class MovieListFragment : Fragment(), MovieDetailFragment.OnWorkCompletedListener {

    private var _binding: FragmentMovieListBinding? = null
    private val binding: FragmentMovieListBinding
        get() = _binding ?: throw RuntimeException("FragmentMovieListBinding can't be null")

    private val viewModel by lazy {
        ViewModelProvider(this)[MovieListViewModel::class.java]
    }

    private val moviesAdapter = MoviesAdapter()
    private lateinit var onNetworkErrorListener: OnNetworkErrorListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnNetworkErrorListener) {
            onNetworkErrorListener = context
        } else {
            throw RuntimeException("Activity must implement OnNetworkErrorListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        for (i in 0 until childFragmentManager.backStackEntryCount) {
            childFragmentManager.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutNetworkError.btnRetry.setOnClickListener {
            viewModel.loadMovies()
        }
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvMovies.adapter = moviesAdapter.apply {
            onReachEnd = viewModel::loadMovies
            onMovieLongCLick = viewModel::changeFavouriteState
            onMovieClick = {
                if (isLandscapeMode()) {
                    launchMovieDetailFragment(it)
                } else {
                    launchMovieDetailActivity(it)
                }
            }
        }
    }


    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) {
            moviesAdapter.submitList(it)
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progress.isVisible = it
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.layoutNetworkError.root.isVisible = it
            if (moviesAdapter.itemCount > 0 && it) {
                binding.layoutNetworkError.root.isVisible = false
                onNetworkErrorListener.onNetworkError()
            }
        }
        viewModel.addToFavouriteError.observe(viewLifecycleOwner) {
            if (it == false) {
                return@observe
            }
            val activity = requireActivity()
            Toast.makeText(
                activity,
                getString(R.string.add_to_favourite_error_message),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.shouldReloadMovieList.observe(viewLifecycleOwner) {
            viewModel.reloadMovieList()
        }
    }

    private fun isLandscapeMode() = binding.movieDetailContainer != null

    private fun launchMovieDetailActivity(movie: Movie) {
        val activity = requireActivity()
        activity.startActivity(
            MovieDetailActivity.newIntent(activity, movie.id, MovieDetailActivity.MODE_POPULAR)
        )
    }

    private fun launchMovieDetailFragment(movie: Movie) {
        childFragmentManager.popBackStack()
        childFragmentManager.beginTransaction()
            .replace(
                R.id.movie_detail_container,
                MovieDetailFragment.newInstance(movie.id, MovieDetailActivity.MODE_POPULAR)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnNetworkErrorListener {
        fun onNetworkError()
    }

    companion object {
        fun newInstance() = MovieListFragment()
    }

    override fun onWorkCompleted() {
        childFragmentManager.popBackStack()
    }
}