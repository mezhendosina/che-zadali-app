package com.mezhendosina.sgo.app.ui

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun errorDialog(
    context: Context,
    message: String,
    withNeutralButton: Boolean = false,
    onClickRestart: () -> Unit = {}
) {
    if (withNeutralButton) MaterialAlertDialogBuilder(context)
        .setTitle("Ошибка")
        .setMessage(message)
        .setPositiveButton("Oк") { dialog, _ -> dialog.cancel() }
        .setNeutralButton("Повторить попытку") { dialog, _ ->
            dialog.cancel()
            onClickRestart.invoke()
        }
        .show()
    else MaterialAlertDialogBuilder(context).setTitle("Ошибка")
        .setMessage(message)
        .setPositiveButton("Oк") { dialog, _ -> dialog.cancel() }
        .show()
}