package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.moment

data class UpdateMomentDTO(
    var id: Int,
    var name: String,
    var orderNumber: Int,
    var content: String,
    var latitude: Double,
    var longitude: Double
)