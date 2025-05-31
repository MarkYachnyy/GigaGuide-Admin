package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api.UserAPI
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.UserDataDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils


class UserRepository() {

    private val userAPI = Retrofit.Builder().baseUrl("${ServerUtils.SERVER_ADDRESS}/api/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build().create(UserAPI::class.java)

    suspend fun getUserData(token: String): UserDataDTO? {
        var response: Response<UserDataDTO> = userAPI.getUserData("Bearer " + token).execute()
        return if (response.isSuccessful) response.body() else null
    }
}