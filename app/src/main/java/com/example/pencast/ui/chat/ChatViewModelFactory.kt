package com.example.pencast.ui.chat

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pencast.login.User

class ChatViewModelFactory(
    private val application: Application,
    private val receiver: User
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(application, receiver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}