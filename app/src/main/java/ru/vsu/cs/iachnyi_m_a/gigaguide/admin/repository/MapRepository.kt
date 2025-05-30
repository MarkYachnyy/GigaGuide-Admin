package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api.MapAPI
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.MapPoint
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils

class MapRepository() {

    private val mapAPI: MapAPI = Retrofit.Builder().baseUrl("${ServerUtils.SERVER_ADDRESS}/api/map/")
        .addConverterFactory(GsonConverterFactory.create()).build().create(MapAPI::class.java)

    suspend fun getCoordinatedOfSight(sightId: Long): MapPoint? {
        var response = mapAPI.getSightCoordinates(sightId).execute()
        return if (response.isSuccessful) {
            MapPoint(latitude = response.body()!!.latitude, longitude = response.body()!!.longitude)
        } else {
            null
        }
    }

    suspend fun getCoordinatesOfMoment(momentId: Long): MapPoint? {
        var response = mapAPI.getMomentCoordinates(momentId).execute()
        return if (response.isSuccessful) {
            MapPoint(latitude = response.body()!!.latitude, longitude = response.body()!!.longitude)
        } else {
            null
        }
    }

}