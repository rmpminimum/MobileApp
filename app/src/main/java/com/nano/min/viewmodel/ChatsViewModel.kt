package com.nano.min.viewmodel

import android.app.Application
import com.nano.min.network.AuthService

data class ChatsUiState(

)

class ChatsViewModel(
    application: Application,
    private val authService: AuthService
) : ViewModelRes(application) {