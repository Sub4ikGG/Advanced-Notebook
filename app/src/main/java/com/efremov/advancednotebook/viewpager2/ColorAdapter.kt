package com.efremov.advancednotebook.viewpager2

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter.POSITION_NONE
import androidx.viewpager2.adapter.FragmentStateAdapter


class ColorAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    private var colors = emptyList<Int>()
    private var currentFont = "sans-serif"

    override fun getItemCount(): Int {
        return colors.count()
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ColorFragment()
        fragment.arguments = Bundle().apply {
            putInt(COLOR_PARAM, colors[position])
            putString(FONT_PARAM, currentFont)
        }
        return fragment
    }

    fun put(list: List<Int>) {
        colors = list
    }

    fun setFont(font: String) {
        currentFont = font
    }
}