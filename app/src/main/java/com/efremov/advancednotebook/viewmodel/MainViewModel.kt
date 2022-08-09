package com.efremov.advancednotebook.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.efremov.advancednotebook.data.Note
import com.efremov.advancednotebook.room.NoteRepository
import java.util.*
import javax.inject.Inject

class MainViewModel: ViewModel() {
    @Inject
    lateinit var repository: NoteRepository
    val allData = MutableLiveData<MutableList<Note>>()

    fun update() {
        Log.d("Bug", "Update called")
        try {
            allData.postValue(repository.getNotes() as MutableList<Note>)
        }
        catch (e: Exception) {
            Log.d("Bug", e.message.toString())
        }
    }

    suspend fun deleteNote(note: Note) {
        val data = allData.value
        data?.remove(note)
        allData.postValue(data)

        repository.deleteNote(note)
    }

    suspend fun updateNote(note: Note) {
        repository.updateNote(note)
    }

    fun swapNotes(fromNote: Note, toNote: Note): Pair<Note, Note> {
        println("Swap..")
        val tempNote = fromNote.copy()

        val data = allData.value
        data?.let {
            Collections.swap(it, it.indexOf(fromNote), it.indexOf(toNote))
            allData.value = data
        }

        fromNote.text = toNote.text
        fromNote.title = toNote.title
        fromNote.time = toNote.time
        fromNote.font = toNote.font
        fromNote.topColor = toNote.topColor

        toNote.text = tempNote.text
        toNote.title = tempNote.title
        toNote.time = tempNote.time
        toNote.font = tempNote.font
        toNote.topColor = tempNote.topColor

        return Pair(fromNote, toNote)
    }
}