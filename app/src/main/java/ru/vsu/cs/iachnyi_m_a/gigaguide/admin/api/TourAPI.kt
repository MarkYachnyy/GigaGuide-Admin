package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.tour.TourDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.tour.PreviewTourDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.ServerResponseMessageDTO

interface TourAPI {

    @GET("tours")
    fun getTourById(@Query("id") id: Long): Call<TourDTO>

    @GET("tours/search")
    fun searchTours(@Query("name") name: String): Call<List<PreviewTourDTO>>

    @Multipart
    @POST("tours")
    fun createTour(@Part image: MultipartBody.Part, @Part tourJSON: MultipartBody.Part): Call<String>

    @Multipart
    @PUT("tours")
    fun updateTourWithImage(@Part image: MultipartBody.Part, @Part updateTourJSON: MultipartBody.Part): Call<String>

    @Multipart
    @PUT("tours")
    fun updateTourWithoutImage(@Part updateTourJSON: MultipartBody.Part): Call<String>

    @DELETE("tours")
    fun deleteTour(@Query("id") id: Int): Call<String>
}