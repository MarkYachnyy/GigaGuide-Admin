package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CurrentLoginState {
    companion object{
        var logged by mutableStateOf(false)
    }
}