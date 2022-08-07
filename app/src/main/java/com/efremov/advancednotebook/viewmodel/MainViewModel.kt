package com.efremov.advancednotebook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.efremov.advancednotebook.data.Note
import com.efremov.advancednotebook.room.NoteRepository
import javax.inject.Inject

class MainViewModel: ViewModel() {
    @Inject
    lateinit var repository: NoteRepository

    lateinit var allData: LiveData<List<Note>>

    fun update() {
        try {
            allData = repository.getNotes()
        }
        catch (e: Exception) {
            println(e.message)
        }
    }
}