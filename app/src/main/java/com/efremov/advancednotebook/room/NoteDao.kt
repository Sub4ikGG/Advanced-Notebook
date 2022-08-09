package com.efremov.advancednotebook.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.efremov.advancednotebook.data.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): List<Note>

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun dropTable()

}