package com.efremov.advancednotebook.viewpager2

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FontAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    private var fonts = emptyList<String>()
    private var currentColor = 0

    override fun getItemCount(): Int {
        return fonts.count()
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FontFragment()
        fragment.arguments = Bundle().apply {
            putString(FONT_PARAM, fonts[position])
            putInt(COLOR_PARAM, currentColor)
        }
        return fragment
    }

    fun put(list: List<String>) {
        fonts = list
    }

    fun setColor(color: Int) {
        currentColor = color
    }
}