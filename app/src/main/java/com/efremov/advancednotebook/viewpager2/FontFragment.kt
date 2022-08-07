package com.efremov.advancednotebook.viewpager2

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.efremov.advancednotebook.R

const val FONT_PARAM = "font"

class FontFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_font, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(FONT_PARAM) }?.apply {
            val topBarNote: CardView = view.findViewById(R.id.top_bar_note)
            val title: TextView = view.findViewById(R.id.title_note)
            val text: TextView = view.findViewById(R.id.text_note)
            val time: TextView = view.findViewById(R.id.time_note)

            val card = PreviewNotes.fontMap[getString(FONT_PARAM)]
            val typeface = Typeface.create(getString(FONT_PARAM), Typeface.NORMAL)

            if(getInt(COLOR_PARAM) != 0) topBarNote.setCardBackgroundColor(ContextCompat.getColor(requireContext(), getInt(COLOR_PARAM)))
            title.typeface = typeface
            text.typeface = typeface
            time.typeface = typeface

            title.text = card?.first
            text.text = card?.second
        }
    }
}