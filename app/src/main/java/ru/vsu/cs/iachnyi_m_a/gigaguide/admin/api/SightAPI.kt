package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.ServerResponseMessageDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight.PreviewSightDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight.SightDTO

interface SightAPI {

    @GET("sights")
    fun getSightById(@Query("id") id: Long): Call<SightDTO>

    @GET("sights/search")
    fun searchSights(@Query("name") name: String): Call<List<PreviewSightDTO>>

    @Multipart
    @POST("sights")
    fun createSight(@Part image: MultipartBody.Part, @Part sightJSON: MultipartBody.Part): Call<ServerResponseMessageDTO>
}