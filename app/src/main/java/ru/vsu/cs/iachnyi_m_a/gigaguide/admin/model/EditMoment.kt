package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model

data class EditMoment(
    var moment: MomentInfo?,
    var name: String,
    var content: String,
    var latitude: Double,
    var longitude: Double
)
