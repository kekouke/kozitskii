package com.kekouke.movies.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kekouke.movies.R
import com.kekouke.movies.data.Country
import com.kekouke.movies.data.Genre
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnWorkCompletedListener) {
            onWorkCompletedListener = context
        } else {
            throw RuntimeException("Activity must implement OnWorkCompletedListener")
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

        observeViewModel()
        if (savedInstanceState == null) {
            viewModel.loadMovieDetail(movieId)
        }
    }

    private fun observeViewModel() {
        viewModel.movieDetail.observe(viewLifecycleOwner) {
            with (binding) {
                Glide.with(this@MovieDetailFragment)
                    .load(it.posterUrl)
                    .into(ivPoster)
                tvDescription.text = it.description
                tvName.text = it.name
                ivBack.setOnClickListener {
                    onWorkCompletedListener.onWorkCompleted()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnWorkCompletedListener {
        fun onWorkCompleted()
    }

    companion object {

        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun newInstance(movieId: Int) = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_MOVIE_ID, movieId)
            }
        }
    }
}