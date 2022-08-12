package com.efremov.advancednotebook.di

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.efremov.advancednotebook.R
import com.efremov.advancednotebook.fragments.STORAGE_NAME
import dagger.Module
import dagger.Provides

@Module
class AppModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
    }
}