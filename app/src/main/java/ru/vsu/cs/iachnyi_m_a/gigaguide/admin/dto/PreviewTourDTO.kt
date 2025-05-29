package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto

data class PreviewTourDTO(
    var id :Integer,
    var name: String,
    var distanceKm: Float?,
    var rating: Float,
    var imagePath: String
)