package com.nano.min.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.Login
import androidx.compose.material.icons.sharp.AutoFixHigh
import androidx.compose.material.icons.sharp.Password
import androidx.compose.ui.graphics.vector.ImageVector
import com.nano.min.R

sealed interface Route {
    val title: Int
    val icon: ImageVector
}
data object LoginRoute : Route {
    override val title = R.string.screen_login
    override val icon = Icons.AutoMirrored.Sharp.Login
}

data object RegisterRoute : Route {
    override val title = R.string.screen_register
    override val icon = Icons.Sharp.AutoFixHigh
}

data object ForgotPasswordRoute : Route {
    override val title = R.string.screen_forgotpass
    override val icon = Icons.Sharp.Password
}

data class AppRoute(val screen: Route) : Route {
    override val title = screen.title
    override val icon = screen.icon
}