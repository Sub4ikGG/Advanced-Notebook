package com.efremov.advancednotebook.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var text: String,
    var time: String,
    var topColor: Int? = null,
    var font: String? = null,
)
