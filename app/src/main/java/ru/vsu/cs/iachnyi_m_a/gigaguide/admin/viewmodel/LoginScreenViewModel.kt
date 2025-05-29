package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.datastore.DataStoreManager
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.LoginRequestDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.AuthRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.CurrentLoginState
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.view.LoginScreenError


class LoginScreenViewModel (private val dataStoreManager: DataStoreManager, private val authRepository: AuthRepository) : ViewModel() {

    var emailInput = mutableStateOf<String>("")
    var passwordInput = mutableStateOf<String>("")

    var error = mutableStateOf(LoginScreenError.NONE)
    var loginSuccess = mutableStateOf(false)
    var loginButtonActive = mutableStateOf(true)

    fun loginUser(){
        if (emailInput.value.trim().isEmpty() ||
            passwordInput.value.trim().isEmpty()
        ) {
            error.value = LoginScreenError.FIELDS_EMPTY
        } else {
            error.value = LoginScreenError.NONE
            viewModelScope.launch {
                loginButtonActive.value = false
                var token = ServerUtils.executeNetworkCall { authRepository.login(
                    LoginRequestDTO(
                        email = emailInput.value.trim(),
                        password = passwordInput.value
                    )
                ) }
                loginSuccess.value = token != null
                if (loginSuccess.value) {
                    emailInput.value = ""
                    passwordInput.value = ""
                    dataStoreManager.saveJWT(token!!)
                    CurrentLoginState.logged = true
                } else {
                    error.value = LoginScreenError.LOGIN_ERROR
                }
                loginButtonActive.value = true
            }
        }
    }

}