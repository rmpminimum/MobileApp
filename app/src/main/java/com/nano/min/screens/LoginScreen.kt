package com.nano.min.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import com.nano.min.R
import com.nano.min.navigation.LoginRoute
import com.nano.min.navigation.Route
import com.nano.min.ui.theme.AppButton
import com.nano.min.ui.theme.KeyboardPassword
import com.nano.min.ui.theme.LargeTitle
import com.nano.min.ui.theme.MinTheme
import com.nano.min.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

suspend fun login(login: String, password: String): Boolean {
    delay(1200L) // Simulate network delay
    return true
}

@Composable
fun LoginScreen(
    backStack: SnapshotStateList<Route>,
    screenContext: LoginRoute,
    onLoginSuccess: () -> Unit = {}
) {

    val scope = rememberCoroutineScope { Dispatchers.IO }

    var loginString by remember { mutableStateOf("") }
    var passwordString by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.padding(top = 80.dp))
        Image(
            painterResource(R.drawable.logo_no_back),
            "app logo",
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally)
        )
        LargeTitle(
            stringResource(R.string.screen_login),
            modifier = Modifier.padding(top = 24.dp, bottom = 64.dp)
        )
        OutlinedCard(shape = ShapeDefaults.Medium, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = loginString,
                    onValueChange = {
                        loginString = it
                    },
                    singleLine = true
                )
                Text(
                    text = stringResource(R.string.password),
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = passwordString,
                    onValueChange = {
                        passwordString = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardPassword
                )
                Spacer(Modifier.padding(vertical = 16.dp))
                AppButton(
                    text = stringResource(R.string.login),
                    onClick = {
                        scope.launch {
                            isLoading = true
                            val success = login(loginString, passwordString)
                            isLoading = false
                            if (success) {
                                onLoginSuccess()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    enabled = (loginString.isNotEmpty() && passwordString.isNotEmpty()) && !isLoading
                )
            }
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun Preview() {
    val backStack: SnapshotStateList<Route> = remember { mutableStateListOf(LoginRoute) }
    MinTheme {
        LoginScreen(backStack, LoginRoute)
    }
}