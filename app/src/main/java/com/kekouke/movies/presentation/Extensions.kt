package com.kekouke.movies.presentation

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