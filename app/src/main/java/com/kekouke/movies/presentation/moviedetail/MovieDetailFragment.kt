package com.kekouke.movies.presentation.moviedetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kekouke.movies.R
import com.kekouke.movies.data.model.Country
import com.kekouke.movies.data.model.Genre
import com.kekouke.movies.databinding.FragmentMovieDetailBinding

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding: FragmentMovieDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentMovieDetailBinding can't be null")

    private val viewModel by lazy {
        ViewModelProvider(this)[MovieDetailViewModel::class.java]
    }

    private lateinit var onWorkCompletedListener: OnWorkCompletedListener

    private var movieId: Int = -1
    private var startMode: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onWorkCompletedListener = if (parentFragment != null) {
            if (requireParentFragment() is OnWorkCompletedListener) {
                requireParentFragment() as OnWorkCompletedListener
            } else {
                throw RuntimeException("ParentFragment must implement OnWorkCompletedListener")
            }
        } else {
            if (context is OnWorkCompletedListener) {
                context
            } else {
                throw RuntimeException("Activity must implement OnWorkCompletedListener")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
        Log.d("MovieDetailFragment", "onCreate")
    }

    private fun parseArguments() {
        val args = requireArguments()
        if (!args.containsKey(KEY_MOVIE_ID)) {
            throw RuntimeException("Argument movieId is absent")
        }
        movieId = args.getInt(KEY_MOVIE_ID)
        startMode = args.getInt(KEY_MODE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutNetworkError.btnRetry.setOnClickListener {
            viewModel.loadMovieDetail(movieId)
        }
        binding.ivBack.setOnClickListener {
            onWorkCompletedListener.onWorkCompleted()
        }

        observeViewModel()
        if (savedInstanceState == null) {
            if (startMode == MovieDetailActivity.MODE_POPULAR) {
                viewModel.loadMovieDetail(movieId)
            } else if (startMode == MovieDetailActivity.MODE_FAVOURITE) {
                viewModel.getLocalMovieDetail(movieId)
            } else {
                throw RuntimeException("Unknown mode: $startMode")
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onWorkCompletedListener.onWorkCompleted()
            }
        })
    }

    private fun observeViewModel() {
        viewModel.movieDetail.observe(viewLifecycleOwner) {
            with (binding) {
                Glide.with(this@MovieDetailFragment)
                    .load(it.posterUrl)
                    .placeholder(R.drawable.movie_no_poster)
                    .into(ivPoster)
                tvDescription.text = it.description
                tvName.text = it.name
                setupGenresLabel(tvGenres, it.genres)
                setupCountryLabel(tvCountry, it.countries)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.layoutNetworkError.root.isVisible = it
        }
    }

    private fun setupCountryLabel(tvCountry: TextView, countries: List<Country>) {
        val firstCountry = countries.firstOrNull()
        tvCountry.text = getString(R.string.label_country, firstCountry?.toString() ?: "")
    }

    private fun setupGenresLabel(tvGenres: TextView, genres: List<Genre>) {
        val genresAsString = StringBuilder("")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnWorkCompletedListener {
        fun onWorkCompleted()
    }

    companion object {

        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"
        private const val KEY_MODE = "KEY_MODE"

        fun newInstance(movieId: Int, mode: Int) = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_MOVIE_ID, movieId)
                putInt(KEY_MODE, mode)
            }
        }
    }
}