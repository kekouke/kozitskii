package com.kekouke.movies.presentation

import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout

fun TabLayout.doOnTabSelected(action: (TabLayout.Tab?) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            action.invoke(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    })
}

fun ViewPager2.doOnPageSelected(action: (Int) -> Unit) {
    registerOnPageChangeCallback(object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            action.invoke(position)
        }
    })
}