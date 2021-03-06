package com.mezhendosina.sgo.app.ui

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.SnackbarViewBinding

fun errorDialog(context: Context, message: String) {
    MaterialAlertDialogBuilder(context)
        .setTitle("Ошибка")
        .setMessage(message)
        .setPositiveButton("Oк") { dialog, _ -> dialog.cancel() }
        .show()
}

fun changeThemeAlertDialog(
    context: Context,
    themes: Array<String>,
    currentTheme: Int,
    onThemeChanged: (Int) -> Unit
) {
    var selectedTheme = currentTheme
    MaterialAlertDialogBuilder(context)
        .setTitle("Выбор темы")
        .setPositiveButton("Ок") { dialog, _ ->
            onThemeChanged(selectedTheme)
            dialog.dismiss()
        }
        .setNeutralButton("Отмена") { dialog, _ -> dialog.cancel() }
        .setSingleChoiceItems(themes, selectedTheme) { _, which ->
            selectedTheme = which
        }
        .show()
}

fun updateDialog(context: Context, message: String, onUpdate: () -> Unit) {
    MaterialAlertDialogBuilder(context).setTitle("Доступна новая версия")
        .setMessage(message)
        .setPositiveButton("Обновить") { _, _ ->
            onUpdate()
        }.show().getButton(DialogInterface.BUTTON_POSITIVE).isAllCaps = false
}

