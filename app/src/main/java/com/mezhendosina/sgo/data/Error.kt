package com.mezhendosina.sgo.data

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.app.databinding.ItemLoadErrorBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Error(
    @SerializedName("message")
    val message: String
)
