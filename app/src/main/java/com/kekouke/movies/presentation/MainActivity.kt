package com.kekouke.movies.presentation

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.kekouke.movies.R
import com.kekouke.movies.databinding.ActivityMainBinding
import com.kekouke.movies.presentation.adapters.ViewPagerAdapter
import com.kekouke.movies.presentation.moviedetail.MovieDetailFragment
import com.kekouke.movies.presentation.movielist.MovieListFragment

class MainActivity :
    AppCompatActivity(),
    MovieListFragment.OnNetworkErrorListener {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val popularPageName by lazy {
        getString(R.string.popular)
    }
    private val favouritePageName by lazy {
        getString(R.string.favourite)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewPager()
        addOnBackPressedCallback()
    }

    private fun setupViewPager() {
        binding.pager.adapter = ViewPagerAdapter(this)
        binding.pager.doOnPageSelected { position ->
            binding.tabs?.let {
                if (position != it.selectedTabPosition) {
                    it.getTabAt(position).apply {
                        it.selectTab(this)
                    }
                }
            }
        }
        binding.pager.isUserInputEnabled = isLandscapeMode()
        if (isLandscapeMode()) {
            binding.pager.doOnPageSelected { position ->
                binding.tvPageLabel.text = getPageName(position)
            }
        } else {
            binding.tabs?.doOnTabSelected {
                it?.let {
                    binding.pager.setCurrentItem(it.position, true)
                    binding.tvPageLabel.text = getPageName(it.position)
                }
            }
        }
    }

    private fun isLandscapeMode() = binding.tabs == null

    private fun getPageName(position: Int) = when (position) {
        0 -> popularPageName
        1 -> favouritePageName
        else -> throw IllegalArgumentException("Wrong position argument: $position")
    }

    private fun addOnBackPressedCallback() {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.pager.currentItem == 1) {
                    binding.pager.setCurrentItem(0, true)
                } else {
                    finish()
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_TAB, getSelectedTabPosition())
    }

    private fun getSelectedTabPosition(): Int {
        return binding.tabs?.selectedTabPosition ?: binding.pager.currentItem
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val selectedPosition = savedInstanceState.getInt(KEY_SELECTED_TAB)
        binding.tabs?.let {
            val tab = it.getTabAt(selectedPosition)
            it.selectTab(tab)
        }
    }

    override fun onNetworkError() {
        Toast.makeText(
            this,
            getString(R.string.network_error_message_short),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val KEY_SELECTED_TAB = "KEY_SELECTED_TAB"
    }
}