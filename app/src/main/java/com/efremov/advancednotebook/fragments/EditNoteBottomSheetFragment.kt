package com.efremov.advancednotebook.fragments

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.efremov.advancednotebook.data.Note
import com.efremov.advancednotebook.databinding.EditNoteBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditNoteBottomSheetFragment(var onFabEventListener: OnFabEventListener): BottomSheetDialogFragment() {

    private var _binding: EditNoteBottomSheetBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditNoteBottomSheetBinding.inflate(layoutInflater)

        val note = arguments?.getParcelable<Note>(EDIT_NOTE_ARG)

        note?.let {
            binding.titleEditNote.setText(it.title)
            binding.textEditNote.setText(it.text)

            when(it.time) {
                "0" -> binding.timeNote.text = ""
                "-1" -> binding.timeNote.visibility = View.GONE
                else -> binding.timeNote.text = note.time
            }

            it.font?.let { f -> setupFont(f) }
            it.topColor?.let { c -> setupColor(c, requireContext()); changeFabColor(c) }

            setOnClickListeners(note)
        }

        return binding.root
    }

    private fun setOnClickListeners(note: Note) {
        binding.createFab.setOnClickListener {
            binding.createFab.press {
                val title = binding.titleEditNote.text.toString()
                val text = binding.textEditNote.text.toString()

                if(title.isNotEmpty() && text.isNotEmpty() && (title != note.title || text != note.text)) {
                    note.title = binding.titleEditNote.text.toString()
                    note.text = binding.textEditNote.text.toString()

                    note.time = when (binding.timeNote.visibility) {
                        View.VISIBLE -> binding.timeNote.text.toString()
                        View.INVISIBLE -> "0"
                        View.GONE -> "-1"
                        else -> {
                            ""
                        }
                    }

                    onFabEventListener.onNoteSaved(note)
                    dismiss()
                }
            }
        }

        binding.deleteFab.setOnClickListener {
            binding.deleteFab.press {
                onFabEventListener.onNoteDeleted(note)
                dismiss()
            }
        }

        binding.setReminderFab.setOnClickListener {
            binding.setReminderFab.press {
                /*val calendar = Calendar.getInstance()
                DatePickerDialog(
                    requireContext(),
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()*/
            }
        }

        binding.removeDateFab.setOnClickListener {
            binding.removeDateFab.press {
                when (binding.timeNote.visibility) {
                    View.VISIBLE -> binding.timeNote.visibility = View.INVISIBLE
                    View.INVISIBLE -> binding.timeNote.visibility = View.GONE
                    else -> binding.timeNote.visibility = View.VISIBLE
                }
            }
        }

        binding.shareNoteFab.setOnClickListener {
            binding.shareNoteFab.press {
                if(binding.titleEditNote.text.isNotEmpty() && binding.textEditNote.text.isNotEmpty()) {
                    val title = binding.titleEditNote.text.toString()
                    val content = binding.textEditNote.text.toString()
                    val date = binding.timeNote.text.toString()

                    val shareIntent = Intent().apply {
                        this.action = Intent.ACTION_SEND
                        this.putExtra(Intent.EXTRA_TEXT, "$title\n\n$content\n\nWritten - $date")
                        this.type = "text/plain"
                    }

                    ContextCompat.startActivity(requireContext(), shareIntent, Bundle())
                }
            }
        }

        var scaled = false
        binding.topBarNote.setOnClickListener {
            if (!scaled)
                binding.noteCardView.animate().apply {
                    duration = 1000
                    scaleX(1.6f)
                    scaleY(1.6f)
                }
            else
                binding.noteCardView.animate().apply {
                    duration = 1000
                    scaleX(1f)
                    scaleY(1f)
                }
            scaled = !scaled
        }
    }

    private fun View.press(qux: () -> Unit) {
        this.animate().apply {
            duration = 200
            scaleX(0.8f)
            scaleY(0.8f)
        }.withEndAction {
            qux()
            this.animate().apply {
                duration = 200
                scaleX(1f)
                scaleY(1f)
            }
        }
    }

    private fun changeFabColor(c: Int) {
        val color = ContextCompat.getColor(requireContext(), c)
        binding.topBarNote.setCardBackgroundColor(color)
        binding.createFab.backgroundTintList = ColorStateList.valueOf(color)
        binding.deleteFab.backgroundTintList = ColorStateList.valueOf(color)
        binding.setReminderFab.backgroundTintList = ColorStateList.valueOf(color)
        binding.removeDateFab.backgroundTintList = ColorStateList.valueOf(color)
        binding.shareNoteFab.backgroundTintList = ColorStateList.valueOf(color)
    }

    private fun setupFont(font: String) {
        val typeface = Typeface.create(font, Typeface.NORMAL)
        binding.titleEditNote.typeface = typeface
        binding.textEditNote.typeface = typeface
        binding.timeNote.typeface = typeface
    }

    private fun setupColor(c: Int, context: Context) {
        val color = ContextCompat.getColor(context, c)
        binding.topBarNote.setCardBackgroundColor(color)
    }

    interface OnFabEventListener {
        fun onNoteSaved(note: Note)
        fun onNoteDeleted(note: Note)
    }
}