package com.example.studentstudyplanner.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentstudyplanner.data.local.entity.UserEntity
import com.example.studentstudyplanner.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val currentUser: StateFlow<UserEntity?> = userRepository.loggedInUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun submit(
        isSignUp: Boolean,
        name: String,
        email: String,
        pass: String,
        confirmPass: String
    ) {
        val trimmedName = name.trim()
        val trimmedEmail = email.trim()

        if (trimmedName.isBlank()) {
            _errorMessage.value = "Please enter your full name."
            return
        }

        if (isSignUp) {
            val nameParts = trimmedName.split("\\s+".toRegex())
            if (nameParts.size < 2) {
                _errorMessage.value = "Please enter both your first name and last name (Full Name)."
                return
            }
        }

        if (trimmedEmail.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            _errorMessage.value = "Please enter a valid email address."
            return
        }

        if (pass.isBlank()) {
            _errorMessage.value = "Please enter a password."
            return
        }

        if (isSignUp && pass != confirmPass) {
            _errorMessage.value = "Passwords do not match."
            return
        }

        viewModelScope.launch {
            val result = if (isSignUp) {
                userRepository.signUp(trimmedName, trimmedEmail, pass)
            } else {
                userRepository.logIn(trimmedName, pass)
            }

            if (result.isFailure) {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Authentication failed."
            } else {
                _errorMessage.value = null
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    class Factory(
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(userRepository) as T
        }
    }
}
