package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.ReviewDTO

interface TourReviewAPI {
    @GET("tours")
    fun getAll(@Header("Authorization") token: String, @Query("tourId") tourId: Int): Call<ReviewDTO>

    @DELETE("tours")
    fun deleteReview(@Header("Authorization") token: String, @Query("reviewId") reviewId: Int): Call<String>
}