package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.JWTResponse
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.LoginRequestDTO

interface AuthAPI {

    @POST("admin/login")
    fun login(@Body loginRequestDTO: LoginRequestDTO): Call<JWTResponse>
}