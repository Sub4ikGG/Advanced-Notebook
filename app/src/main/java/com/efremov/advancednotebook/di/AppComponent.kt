package com.efremov.advancednotebook.di

import com.efremov.advancednotebook.fragments.CreateNoteFragment
import com.efremov.advancednotebook.MainActivity
import com.efremov.advancednotebook.fragments.EditNoteBottomSheetFragment
import com.efremov.advancednotebook.fragments.MainFragment
import com.efremov.advancednotebook.fragments.SettingsFragment
import com.efremov.advancednotebook.recyclerview.MainAdapter
import com.efremov.advancednotebook.viewmodel.MainViewModel
import dagger.Component

@Component(modules = [AppModule::class, DatabaseModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(createNoteFragment: CreateNoteFragment)
    fun inject(mainViewModel: MainViewModel)
    fun inject(adapter: MainAdapter)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(editNoteBottomSheetFragment: EditNoteBottomSheetFragment)
}