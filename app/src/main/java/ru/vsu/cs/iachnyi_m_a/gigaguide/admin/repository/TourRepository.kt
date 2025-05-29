package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository

import android.util.Log
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api.TourAPI
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.PreviewTourDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.TourDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightTourThumbnail
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.TourInfo
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.dto.mapper.TourDTOMapper

class TourRepository() {

    private val tourAPI = Retrofit.Builder().baseUrl("${ServerUtils.SERVER_ADDRESS}/api/tour-sight/")
        .addConverterFactory(GsonConverterFactory.create()).build().create(TourAPI::class.java)

    suspend fun getTourInfoById(id: Long): TourInfo? {
        var call = tourAPI.getTourById(id)
        var response: Response<TourDTO> = call.execute()
        return if (response.isSuccessful) {
            TourDTOMapper().map(response.body()!!)
        } else {
            null
        }
    }

    suspend fun searchTours(name: String): List<SightTourThumbnail>? {
        var call = tourAPI.searchTours(name)
        var response: Response<List<PreviewTourDTO>> = call.execute()
        return if (response.isSuccessful) {
            response.body()!!.map { dto -> SightTourThumbnail(sightId = dto.id.toLong(), rating = dto.rating, name = dto.name, proximity = 0f, imageLink = dto.imagePath) }
        } else {
            null
        }
    }
}