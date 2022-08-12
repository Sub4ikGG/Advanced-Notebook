package com.efremov.advancednotebook

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

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

fun showSnackbarMessage(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        .setAction("Action", null).show()
}