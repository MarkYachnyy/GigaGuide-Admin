package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository

import ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.model.review.ReviewSet

interface ReviewRepository {

    suspend fun getAllReviews(objectId: Int): ReviewSet?
    suspend fun deleteReview(token: String, id: Int): Boolean?

}