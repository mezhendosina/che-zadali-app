package com.che.zadali.sgoapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.che.zadali.sgoapp.data.layout.messageList.Record
import com.che.zadali.sgoapp.data.services.MessagesActionListener
import com.che.zadali.sgoapp.data.services.MessagesService

class MessagesViewModel(private val messagesService: MessagesService) : ViewModel() {

    private var _messages = MutableLiveData<List<Record>>()
    var messages: LiveData<List<Record>> = _messages

    private var _loaded = MutableLiveData(false)
    var loaded: LiveData<Boolean> = _loaded

    private val listener: MessagesActionListener = {
        _messages.value = it
        _loaded.value = !_messages.value.isNullOrEmpty()
    }

    init {
        loadMessages()
    }

    private fun loadMessages() {
        messagesService.addListener(listener)
        messagesService.loadMessages()
    }

    override fun onCleared() {
        super.onCleared()
        messagesService.removeListener(listener)
    }
}

class MessagesItemViewModel(private val messagesService: MessagesService) : ViewModel() {
    private var _message = MutableLiveData<Record>()
    var message: LiveData<Record> = _message

    fun loadMessage(messageId: Int) {
        _message.value = messagesService.findMessage(messageId)
    }
}