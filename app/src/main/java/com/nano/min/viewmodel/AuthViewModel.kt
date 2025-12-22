package com.nano.min.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nano.min.network.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthMode {
    Login, Register, ForgotPassword
}

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel(
    application: Application,
    private val authService: AuthService
) : ViewModelRes(application) {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, error = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }

    fun authenticate(mode: AuthMode) {
        val currentState = _uiState.value
        when (mode) {
            AuthMode.Login, AuthMode.Register -> {
                if (currentState.email.isEmpty() || currentState.password.isEmpty()) {
                    _uiState.value = currentState.copy(error = "Email and password are required")
                    return
                }
            }
            AuthMode.ForgotPassword -> {
                if (currentState.email.isEmpty()) {
                    _uiState.value = currentState.copy(error = "Email is required")
                    return
                }
            }
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, error = null)
            try {
                val success = when (mode) {
                    AuthMode.Login -> authService.login(currentState.email, currentState.password)
                    AuthMode.Register -> authService.register(currentState.email, currentState.password)
                    AuthMode.ForgotPassword -> {
                        // Stub: simulate sending reset email
                        kotlinx.coroutines.delay(1000) // simulate delay
                        true
                    }
                }
                _uiState.value = if (success) {
                    currentState.copy(isLoading = false, isSuccess = true)
                } else {
                    currentState.copy(isLoading = false, error = "${mode.name} failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = currentState.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: e.message ?: "${mode.name} failed"
                )
            }
        }
    }

    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}
