package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.navigation

import kotlinx.serialization.Serializable

@Serializable
object SightTourListScreenObject

@Serializable
data class EditSightScreenClass(
    val sightId: Long
)
@Serializable
data class EditTourScreenClass(
    val tourId: Long
)

@Serializable
data class SightReviewScreenClass(
    val sightId: Long
)
@Serializable
data class TourReviewScreenClass(
    val tourId: Long
)
@Serializable
object CreateSightScreenObject
@Serializable
object CreateTourScreenObject