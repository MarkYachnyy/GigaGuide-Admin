package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.tour

data class PreviewTourDTO(
    var id :Integer,
    var name: String,
    var distanceKm: Float?,
    var rating: Float,
    var imagePath: String,
    var latitude: Double,
    var longitude: Double
)