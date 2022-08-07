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
import com.efremov.advancednotebook.R

/**
 * A simple [Fragment] subclass.
 * Use the [ColorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

const val COLOR_PARAM = "color"

class ColorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(COLOR_PARAM) }?.apply {
            val topBarNote: CardView = view.findViewById(R.id.top_bar_note)
            val title: TextView = view.findViewById(R.id.title_note)
            val text: TextView = view.findViewById(R.id.text_note)
            val time: TextView = view.findViewById(R.id.time_note)

            val card = PreviewNotes.colorMap[getInt(COLOR_PARAM)]
            val typeface = Typeface.create(getString(FONT_PARAM), Typeface.NORMAL)

            topBarNote.setCardBackgroundColor(ContextCompat.getColor(requireContext(), getInt(COLOR_PARAM)))
            title.text = card?.first; title.typeface = typeface
            text.text = card?.second; text.typeface = typeface; time.typeface = typeface
        }
    }
}