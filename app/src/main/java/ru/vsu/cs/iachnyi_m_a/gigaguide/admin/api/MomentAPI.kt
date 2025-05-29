package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.moment.MomentDTO

interface MomentAPI {
    @GET("sight")
    fun getAllMoments(@Query("sightId") sightId: Long): Call<List<MomentDTO>>
}