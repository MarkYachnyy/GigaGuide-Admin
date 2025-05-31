package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api.SightReviewAPI
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.ReviewsDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.mapper.review.ReviewsDTOMapper
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.model.review.ReviewSet

class SightReviewRepository() :
    ReviewRepository {

    private val sightReviewAPI: SightReviewAPI = Retrofit.Builder().baseUrl("${ServerUtils.SERVER_ADDRESS}/api/reviews/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build().create(SightReviewAPI::class.java)

    override suspend fun getAllReviews(
        objectId: Int
    ): ReviewSet? {
        var response: Response<ReviewsDTO> =
            sightReviewAPI.getAll("Bearer ", objectId).execute()
        return if (response.isSuccessful) {
            ReviewsDTOMapper().map(response.body()!!)
        } else {
            null
        }
    }

    override suspend fun deleteReview(token: String, id: Int): Boolean? {
        var response: Response<String> = sightReviewAPI.deleteReview("Bearer $token", id).execute()
        return response.isSuccessful
    }

}