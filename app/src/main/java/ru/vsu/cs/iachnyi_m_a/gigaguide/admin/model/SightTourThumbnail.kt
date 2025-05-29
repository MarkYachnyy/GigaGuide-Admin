package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model

data class SightTourThumbnail(
    var sightId: Long,
    var name: String,
    var rating: Float,
    var proximity: Float,
    var imageLink: String
)
