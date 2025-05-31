package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.tour

data class UpdateTourDTO(
    var id: Int,
    var name: String,
    var description: String,
    var category: String,
    var type: String,
    var city: String
)
