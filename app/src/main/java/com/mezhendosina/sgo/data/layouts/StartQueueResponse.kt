package com.mezhendosina.sgo.data.layouts

data class StartQueueResponse(
    val activeConnectionExec: Boolean,
    val cancellable: Boolean,
    val queueKey: String,
    val taskId: Int
)