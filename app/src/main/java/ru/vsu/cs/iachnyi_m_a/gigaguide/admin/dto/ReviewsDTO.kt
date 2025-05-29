package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto

data class ReviewsDTO(
    var userReview: ReviewDTO?,
    var otherReviews: List<ReviewDTO>
)
