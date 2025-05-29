package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight

data class UpdateSightDTO(
    var id: Int,
    var name: String,
    var description: String,
    var city: String,
    var latitude: Double,
    var longitude: Double
)
