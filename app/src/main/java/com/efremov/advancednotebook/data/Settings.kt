package com.efremov.advancednotebook.data

import com.efremov.advancednotebook.R

data class Settings(
    var layout: String = "",
    var confirm: Boolean = false,
    var paintEnvironment: Boolean = false,
    var baseColor: Int = R.color.default_note_color,
    var baseFont: String = "sans-serif"
)
