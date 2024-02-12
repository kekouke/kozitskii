package com.kekouke.movies.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kekouke.movies.presentation.favourite.FavouriteMoviesFragment
import com.kekouke.movies.presentation.movielist.MovieListFragment

class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MovieListFragment.newInstance()
        1 -> FavouriteMoviesFragment.newInstance()
        else -> throw IllegalArgumentException("Wrong position argument: $position")
    }
}