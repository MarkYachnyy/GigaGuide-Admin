package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight

data class PreviewSightDTO(
    var id: Int,
    var name: String,
    var latitude: Float,
    var longitude: Float,
    var rating: Float,
    var imagePath: String
)