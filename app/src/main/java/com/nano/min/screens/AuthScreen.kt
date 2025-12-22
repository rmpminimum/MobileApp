package com.nano.min.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.inspectable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nano.min.R
import com.nano.min.ui.theme.AppButton
import com.nano.min.ui.theme.KeyboardPassword
import com.nano.min.ui.theme.LargeTitle
import com.nano.min.ui.theme.MinTheme
import com.nano.min.ui.theme.Typography
import com.nano.min.viewmodel.AuthMode
import com.nano.min.viewmodel.AuthUiState
import com.nano.min.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    initialMode: AuthMode = AuthMode.Login,
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    var mode by remember { mutableStateOf(initialMode) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onAuthSuccess()
            viewModel.resetSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.padding(top = 80.dp))
        AuthLogo()
        AuthAnimatedContent(mode, uiState, viewModel) { newMode -> mode = newMode }
    }
}

@Composable
private fun AuthLogo() {
    Image(
        painterResource(R.drawable.logo),
        "app logo",
        modifier = Modifier
            .size(240.dp)
    )
}

@Composable
private fun AuthAnimatedContent(
    mode: AuthMode,
    uiState: AuthUiState,
    viewModel: AuthViewModel,
    onModeChange: (AuthMode) -> Unit
) {
    AnimatedContent(
        targetState = mode,
        transitionSpec = {
            slideInHorizontally { if (targetState.ordinal > initialState.ordinal) it else -it } + fadeIn() togetherWith
                    slideOutHorizontally { if (targetState.ordinal > initialState.ordinal) -it else it } + fadeOut()
        },
        label = "AuthModeTransition"
    ) { currentMode ->
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LargeTitle(
                text = when (currentMode) {
                    AuthMode.Login -> stringResource(R.string.screen_login)
                    AuthMode.Register -> stringResource(R.string.screen_register)
                    AuthMode.ForgotPassword -> stringResource(R.string.screen_forgotpass)
                },
                modifier = Modifier.padding(top = 24.dp, bottom = 64.dp)
            )
            when (currentMode) {
                AuthMode.Login -> LoginForm(uiState, viewModel, onModeChange)
                AuthMode.Register -> RegisterForm(uiState, viewModel, onModeChange)
                AuthMode.ForgotPassword -> ForgotPasswordForm(uiState, viewModel, onModeChange)
            }
        }
    }
}

@Composable
private fun LoginForm(
    uiState: AuthUiState,
    viewModel: AuthViewModel,
    onModeChange: (AuthMode) -> Unit
) {
    OutlinedCard(shape = ShapeDefaults.Medium, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.email),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                singleLine = true
            )
            Text(
                text = stringResource(R.string.password),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                singleLine = true,
                keyboardOptions = KeyboardPassword
            )
            Spacer(Modifier.padding(vertical = 16.dp))
            AppButton(
                text = stringResource(R.string.login),
                onClick = { viewModel.authenticate(AuthMode.Login) },
                modifier = Modifier.fillMaxWidth(),
                enabled = (uiState.email.isNotEmpty() && uiState.password.isNotEmpty()) && !uiState.isLoading
            )
            TextButton(
                onClick = { onModeChange(AuthMode.ForgotPassword) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.forgotpass))
            }
            TextButton(
                onClick = { onModeChange(AuthMode.Register) },
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(stringResource(R.string.register_rationale))
            }
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    style = Typography.bodyMedium,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun RegisterForm(
    uiState: AuthUiState,
    viewModel: AuthViewModel,
    onModeChange: (AuthMode) -> Unit
) {
    OutlinedCard(shape = ShapeDefaults.Medium, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.email),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                singleLine = true
            )
            Text(
                text = stringResource(R.string.password),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                singleLine = true,
                keyboardOptions = KeyboardPassword
            )
            Spacer(Modifier.padding(vertical = 16.dp))
            AppButton(
                text = stringResource(R.string.register),
                onClick = { viewModel.authenticate(AuthMode.Register) },
                modifier = Modifier.fillMaxWidth(),
                enabled = (uiState.email.isNotEmpty() && uiState.password.isNotEmpty()) && !uiState.isLoading
            )
            TextButton(
                onClick = { onModeChange(AuthMode.Login) },
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(stringResource(R.string.login_rationale))
            }
            if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    style = Typography.bodyMedium,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ForgotPasswordForm(
    uiState: AuthUiState,
    viewModel: AuthViewModel,
    onModeChange: (AuthMode) -> Unit
) {
    OutlinedCard(shape = ShapeDefaults.Medium, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.forgot_description),
                style = Typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )
            Text(
                text = stringResource(R.string.email),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                singleLine = true
            )
            Spacer(Modifier.padding(vertical = 16.dp))
            AppButton(
                text = stringResource(R.string.send_reset),
                onClick = { viewModel.authenticate(AuthMode.ForgotPassword) },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.email.isNotEmpty() && !uiState.isLoading
            )
            TextButton(
                onClick = { onModeChange(AuthMode.Login) },
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(stringResource(R.string.back_to_login))
            }
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    style = Typography.bodyMedium,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun Preview() {
    MinTheme {
        AuthScreen(onAuthSuccess = {})
    }
}
