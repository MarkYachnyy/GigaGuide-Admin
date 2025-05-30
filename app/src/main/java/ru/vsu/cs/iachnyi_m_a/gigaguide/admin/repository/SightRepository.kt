package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository

import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api.SightAPI
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.ServerResponseMessageDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.mapper.SightDTOMapper
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight.CreateSightDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight.PreviewSightDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight.SightDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight.UpdateSightDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightInfo
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightSearchResult
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import java.io.File

class SightRepository() {

    private val sightAPI: SightAPI =
        Retrofit.Builder().baseUrl("${ServerUtils.SERVER_ADDRESS}/api/tour-sight/")
            .addConverterFactory(GsonConverterFactory.create()).addConverterFactory(
                ScalarsConverterFactory.create()
            ).build().create(SightAPI::class.java)

    suspend fun getSightInfoById(id: Long): SightInfo? {
        var call = sightAPI.getSightById(id)
        var response: Response<SightDTO> = call.execute()
        return if (response.isSuccessful) {
            SightDTOMapper().map(response.body()!!)
        } else {
            null
        }
    }

    suspend fun search(name: String): List<SightSearchResult>? {
        var response: Response<List<PreviewSightDTO>> = sightAPI.searchSights(name).execute()
        return if (response.isSuccessful) {
            response.body()!!.map { dto ->
                SightSearchResult(
                    id = dto.id,
                    name = dto.name,
                    imageLink = dto.imagePath,
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    rating = dto.rating
                )
            }
        } else {
            null
        }
    }

    suspend fun create(
        image: File,
        name: String,
        description: String,
        city: String,
        latitude: Double,
        longitude: Double
    ): Boolean? {
        var resp = sightAPI.createSight(
            image = MultipartBody.Part.createFormData(
                name = "image",
                filename = "image.jpg",
                body = image.asRequestBody()
            ),
            sightJSON = MultipartBody.Part.createFormData(
                name = "sight", value = Gson().toJson(
                    CreateSightDTO(
                        name = name,
                        description = description,
                        city = city,
                        latitude = latitude,
                        longitude = longitude
                    )
                )
            )
        ).execute()
        return resp.isSuccessful
    }

    suspend fun update(
        image: File?, sightId: Int, name: String,
        description: String,
        city: String,
        latitude: Double,
        longitude: Double
    ): Boolean? {
        var resp: Response<ServerResponseMessageDTO>
        if (image == null) {
            resp = sightAPI.updateSightWithoutImage(
                updateSightJSON = MultipartBody.Part.createFormData(
                    name = "sight", value = Gson().toJson(
                        UpdateSightDTO(
                            id = sightId,
                            name = name,
                            description = description,
                            city = city,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                )
            ).execute()
        } else {
            resp = sightAPI.updateSightWithImage(
                image = MultipartBody.Part.createFormData(
                    name = "image",
                    filename = "image.jpg",
                    body = image.asRequestBody()
                ),
                updateSightJSON = MultipartBody.Part.createFormData(
                    name = "sight", value = Gson().toJson(
                        UpdateSightDTO(
                            id = sightId,
                            name = name,
                            description = description,
                            city = city,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                )
            ).execute()
        }

        return resp.isSuccessful
    }

}