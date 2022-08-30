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