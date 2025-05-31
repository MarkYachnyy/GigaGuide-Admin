package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.tour

import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight.PreviewSightDTO

data class TourDTO (
    var id: Int,
    var name: String,
    var description: String,
    var city: String,
    var durationMinutes: Int,
    var distanceKm: Float,
    var category: String,
    var type: String,
    var rating: Float,
    var imagePath: String,
    var sights: List<PreviewSightDTO>
)