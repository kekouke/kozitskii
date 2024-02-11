package com.kekouke.movies.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.kekouke.movies.R
import com.kekouke.movies.databinding.ActivityMainBinding

class MainActivity :
    AppCompatActivity(),
    MovieListFragment.OnNetworkErrorListener,
    MovieDetailFragment.OnWorkCompletedListener {

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

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
    }

    private fun isLandscapeMode() = binding.tabs == null

    private fun setupViewPager() {
        binding.pager.adapter = ViewPagerAdapter(this)
        if (isLandscapeMode()) {
            binding.pager.isUserInputEnabled = true
            binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tvPageLabel.text = getPageName(position)
                }
            })
        } else {
            binding.pager.isUserInputEnabled = false
            binding.tabs?.doOnTabSelected {
                it?.let {
                    binding.pager.setCurrentItem(it.position, true)
                    binding.tvPageLabel.text = getPageName(it.position)
                }
            }
        }
    }

    private fun getPageName(position: Int) = when (position) {
        0 -> popularPageName
        1 -> favouritePageName
        else -> throw IllegalArgumentException("Wrong position argument: $position")
    }

    override fun onNetworkError() {
        Toast.makeText(
            this,
            getString(R.string.network_error_message_short),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onWorkCompleted() {
        onBackPressedDispatcher.onBackPressed()
    }
}