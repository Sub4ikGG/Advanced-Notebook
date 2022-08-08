package com.efremov.advancednotebook.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.efremov.advancednotebook.R
import com.efremov.advancednotebook.data.Note
import com.efremov.advancednotebook.databinding.TextNoteLayoutBinding
import com.efremov.advancednotebook.room.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainAdapter(var onNoteClickListener: OnNoteClickListener): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var repository: NoteRepository

    private var notes = ArrayList<Note>()

    inner class MainViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnLongClickListener {
        private val binding = TextNoteLayoutBinding.bind(view)

        fun bind(note: Note, context: Context) {
            binding.titleNote.text = note.title
            binding.textNote.text = note.text

            when(note.time) {
                "0" -> binding.timeNote.text = ""
                "-1" -> binding.timeNote.visibility = View.GONE
                else -> binding.timeNote.text = note.time
            }

            note.font?.let { setupFont(it) }
            note.topColor?.let { setupColor(it, context) }

            binding.topBarNote.setOnLongClickListener(this)
        }

        private fun setupFont(font: String) {
            val typeface = Typeface.create(font, Typeface.NORMAL)
            binding.titleNote.typeface = typeface
            binding.textNote.typeface = typeface
            binding.timeNote.typeface = typeface
        }

        private fun setupColor(c: Int, context: Context) {
            val color = ContextCompat.getColor(context, c)
            binding.topBarNote.setCardBackgroundColor(color)
        }

        override fun onLongClick(p0: View?): Boolean {
            onNoteClickListener.onNoteClick(adapterPosition)
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_note_layout, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(notes[position], context)
    }

    override fun getItemCount(): Int {
        return notes.count()
    }

    fun getItems(): ArrayList<Note> = notes

    fun deleteItem(note: Note, adapterPosition: Int) {
        notes.remove(note)
        notifyItemRemoved(adapterPosition)
    }

    interface OnNoteClickListener {
        fun onNoteClick(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun put(array: ArrayList<Note>) {
        notes = array
        notifyDataSetChanged()
    }
}