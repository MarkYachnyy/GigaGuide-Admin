package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api.MomentAPI
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.moment.MomentDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.MomentInfo
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.dto.mapper.MomentDTOMapper

class MomentRepository {

    private val momentAPI =
        Retrofit.Builder().baseUrl("${ServerUtils.SERVER_ADDRESS}/api/tour-sight/moments/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(MomentAPI::class.java)

    suspend fun getSightMoments(sightId: Long): List<MomentInfo>? {
        var response: Response<List<MomentDTO>> =
            momentAPI.getAllMoments(sightId = sightId).execute()
        return if (response.isSuccessful) {
            response.body()!!.sortedBy { it.orderNumber }
                .map { MomentDTOMapper().map(it) }
        } else {
            null
        }
    }
}