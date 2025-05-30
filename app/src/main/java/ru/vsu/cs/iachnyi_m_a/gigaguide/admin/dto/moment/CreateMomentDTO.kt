package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.moment

data class CreateMomentDTO(
    var name: String,
    var orderNumber: Int,
    var sightId: Int,
    var content: String,
    var latitude: Double,
    var longitude: Double
)
