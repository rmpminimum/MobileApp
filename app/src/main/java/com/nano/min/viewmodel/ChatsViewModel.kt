package com.nano.min.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nano.min.network.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatsUiState(
    val chats: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

//class ChatsViewModel(
//    application: Application,
//    private val authService: AuthService
//) : AndroidViewModel(application) {
//
//    private val _uiState = MutableStateFlow(ChatsUiState())
//    val uiState: StateFlow<ChatsUiState> = _uiState
//
//    fun loadChats() {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true)
//            try {
//                val chats: Unit = authService.getChats()
//                _uiState.value = _uiState.value.copy(chats = chats, isLoading = false)
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
//            }
//        }
//    }
//}
