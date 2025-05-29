package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api.AuthAPI
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.JWTResponse
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.LoginRequestDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils


class AuthRepository() {

    private val authAPI = Retrofit.Builder().baseUrl("${ServerUtils.SERVER_ADDRESS}/api/auth/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create()
        ).build().create(AuthAPI::class.java)

    suspend fun login(loginRequestDTO: LoginRequestDTO): String? {
        var response: Response<JWTResponse> = authAPI.login(loginRequestDTO).execute()
        return if (response.isSuccessful) {
            response.body()!!.token
        } else {
            null
        }
    }

}