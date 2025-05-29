package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight

import android.accessibilityservice.GestureDescription

data class CreateSightDTO(
    var name: String,
    var description: String,
    var city: String,
    var latitude: Double,
    val longitude: Double
)
