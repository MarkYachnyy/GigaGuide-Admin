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
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.ServerResponseMessageDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.moment.MomentDTO

interface MomentAPI {
    @GET("moments/sight")
    fun getAllMoments(@Query("sightId") sightId: Long): Call<List<MomentDTO>>

    @DELETE("admin/moments")
    fun deleteMoment(@Query("id") momentId: Int): Call<ServerResponseMessageDTO>

    @Multipart
    @POST("admin/moments")
    fun createMoment(@Part image: MultipartBody.Part, @Part momentJSON: MultipartBody.Part): Call<ServerResponseMessageDTO>

    @Multipart
    @PUT("admin/moments")
    fun updateMomentWithImage(@Part image: MultipartBody.Part, @Part updateMomentJSON: MultipartBody.Part): Call<ServerResponseMessageDTO>

    @Multipart
    @PUT("admin/moments")
    fun updateMomentWithoutImage(@Part updateMomentJSON: MultipartBody.Part): Call<ServerResponseMessageDTO>
}