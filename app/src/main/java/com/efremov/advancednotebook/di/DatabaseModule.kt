package com.efremov.advancednotebook.di

import android.content.Context
import com.efremov.advancednotebook.room.NoteDao
import com.efremov.advancednotebook.room.NoteDatabase
import com.efremov.advancednotebook.room.NoteRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    fun provideDatabase(context: Context): NoteDatabase {
        return NoteDatabase.getDatabase(context)
    }

    @Provides
    fun provideDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    fun provideRepository(dao: NoteDao): NoteRepository {
        return NoteRepository(dao)
    }

}