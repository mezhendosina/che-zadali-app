package com.mezhendosina.sgo.app.ui

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun errorDialog(context: Context, message: String) {
    MaterialAlertDialogBuilder(context)
        .setTitle("Ошибка")
        .setMessage(message)
        .setPositiveButton("Oк") { dialog, _ -> dialog.dismiss() }
        .show()
}
