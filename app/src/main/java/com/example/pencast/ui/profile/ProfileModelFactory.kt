package com.example.pencast.ui.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pencast.login.User

class ProfileModelFactory(
    private val application: Application,
    private val profile: User
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(application, profile) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}