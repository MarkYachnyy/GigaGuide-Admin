package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.ReviewDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.ReviewsDTO

interface SightReviewAPI {
    @GET("sights")
    fun getAll(@Header("Authorization") token: String, @Query("sightId") sightId: Int): Call<ReviewsDTO>


    @DELETE("admin/sights")
    fun deleteReview(@Header("Authorization") token: String, @Query("reviewId") reviewId: Int): Call<String>
}