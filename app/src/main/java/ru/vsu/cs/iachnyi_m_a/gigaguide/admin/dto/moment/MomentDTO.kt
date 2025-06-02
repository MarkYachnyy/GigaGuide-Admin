package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.moment

data class MomentDTO(
    var name: String,
    var id: Long,
    var orderNumber: Int,
    var imagePath: String,
    var content: String
)