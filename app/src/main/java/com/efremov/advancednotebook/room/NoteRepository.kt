package com.efremov.advancednotebook.room

import androidx.lifecycle.LiveData
import com.efremov.advancednotebook.data.Note
import javax.inject.Inject

class NoteRepository(private val noteDao: NoteDao) {

    suspend fun insertNote(note: Note) {
        return noteDao.insertNote(note)
    }

    fun getNotes(): LiveData<List<Note>> {
        return noteDao.getAllNotes()
    }

    suspend fun deleteNote(note: Note) {
        return noteDao.deleteNote(note)
    }

    suspend fun dropTable() {
        noteDao.dropTable()
    }
}