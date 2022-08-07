package com.efremov.advancednotebook.viewpager2

import com.efremov.advancednotebook.R

object PreviewNotes {
    private val colorTexts = listOf(
        Pair("Fresh wheat", "Swipe me right to select new color :3"),
        Pair("Mint breath", "Green is soothing"),
        Pair("Ocean blue", "Look right up to the sky.."),
        Pair("Morning lilac", "Girls like it!"),
        Pair("Just brick", "Just brick, nothing suspicious.. (no)")
    )

    private val fontTexts = listOf(
        Pair("Standard cowboy", "Pretty simple and default font"),
        Pair("Thin", "Sharpened pencil"),
        Pair("Super Thin", "Sharpened pencil x2"),
        Pair("Strict dog", "I`m so serious about it.."),
        Pair("Writer", "Pushkin wrote like this, I tell you honestly!"),
        Pair("Brother", "Choose me, please! I`m better than my sister!"),
        Pair("Sister", "Choose who you want, I don't care..")
    )

    val colorMap = mapOf(
        R.color.default_note_color to colorTexts[0],
        R.color.blue_note_color to colorTexts[2],
        R.color.green_note_color to colorTexts[1],
        R.color.pink_note_color to colorTexts[3],
        R.color.red_note_color to colorTexts[4]
    )

    val fontMap = mapOf(
        "sans-serif" to fontTexts[0],
        "sans-serif-light" to fontTexts[1],
        "sans-serif-thin" to fontTexts[2],
        "monospace" to fontTexts[3],
        "cursive" to fontTexts[4],
        "sans-serif-smallcaps" to fontTexts[5],
        "serif" to fontTexts[6]
    )
}
