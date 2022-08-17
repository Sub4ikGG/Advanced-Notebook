package com.efremov.advancednotebook

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import com.efremov.advancednotebook.data.QAModel
import com.google.android.material.snackbar.Snackbar

val qaList = arrayListOf(
    QAModel("What is it", R.string.about_app.toString()),
    QAModel("How to create note", "Click on the button at the bottom left of the main screen, write something in the note at the top by clicking on the hints and create a note by clicking on the checkmark button"),
    QAModel("How to edit/delete note", "Click on the note at the main screen and you will see note editor. Click on the appropriate fields and buttons and then save by clicking on the button with a checkmark"),
    QAModel("How many notes can I create", "You can create an unlimited number of notes (Offline) and 50 free (Online - Soon)"),
    QAModel("How to contact the author", "Go to the section about the application and click on the corresponding icons. I prefer Telegram")
)

fun showAlertDialog(
    context: Context,
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    positivePress: () -> Unit,
    negativePress: () -> Unit = {}
) {
    val builder = android.app.AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(positiveButtonText) { _: DialogInterface, _: Int ->
        positivePress()
    }
    builder.setNegativeButton(negativeButtonText) { _: DialogInterface, _: Int ->
        negativePress()
    }
    builder.show()
}

fun showToastMessage(view: View, message: String) {
    Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
}