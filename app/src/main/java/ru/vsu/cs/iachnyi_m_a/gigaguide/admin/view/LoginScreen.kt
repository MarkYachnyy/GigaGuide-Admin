package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.GigaGuideAdminTheme
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.Green
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.Invisible
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.MediumGrey
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.Red
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.ui.theme.White
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel.LoginScreenViewModel

enum class LoginScreenError{
    FIELDS_EMPTY,
    LOGIN_ERROR,
    NONE
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    multiLine: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Invisible,
            unfocusedIndicatorColor = Invisible,
            cursorColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        placeholder = {
            Text(hint, color = MaterialTheme.colorScheme.onPrimaryContainer)
        },
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = Invisible),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = !multiLine
    )
}

@Composable
fun LoginScreen(
    loginScreenViewModel: LoginScreenViewModel
) {
    GigaGuideAdminTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "Войти",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(20.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                CustomTextField(
                    value = loginScreenViewModel.emailInput.value,
                    hint = "Электронная почта",
                    onValueChange = { loginScreenViewModel.emailInput.value = it },
                    isPassword = false
                )
                CustomTextField(
                    modifier = Modifier
                        .padding(top=25.dp)
                        ,
                    value = loginScreenViewModel.passwordInput.value,
                    hint = "Пароль",
                    onValueChange = { loginScreenViewModel.passwordInput.value = it },
                    isPassword = true
                )
                var text = ""
                var error: LoginScreenError = loginScreenViewModel.error.value
                if (error == LoginScreenError.FIELDS_EMPTY) {
                    text = "Заполните все поля"
                } else if (error == LoginScreenError.LOGIN_ERROR) {
                    text = "Ошибка входа"
                } else if (loginScreenViewModel.loginSuccess.value) {
                    text = "Успешный вход"
                }
                Text(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    text = text,
                    textAlign = TextAlign.Center,
                    color = if (loginScreenViewModel.loginSuccess.value) Green else Red,
                    style = MaterialTheme.typography.titleMedium
                )
                Button(
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = MediumGrey,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = White
                    ),
                    onClick = { loginScreenViewModel.loginUser() },
                    enabled = loginScreenViewModel.loginButtonActive.value
                ) {
                    Text(
                        text = "Войти",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 80.dp)
                    )
                }
            }
        }
    }
}
