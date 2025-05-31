package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository

import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api.TourAPI
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.tour.CreateTourDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.tour.PreviewTourDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.tour.TourDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.tour.UpdateTourDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightTourThumbnail
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.TourInfo
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.dto.mapper.TourDTOMapper
import java.io.File

class TourRepository() {

    private val tourAPI =
        Retrofit.Builder().baseUrl("${ServerUtils.SERVER_ADDRESS}/api/tour-sight/")
            .addConverterFactory(
                ScalarsConverterFactory.create()
            )
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
            response.body()!!.map { dto ->
                SightTourThumbnail(
                    sightId = dto.id.toLong(),
                    rating = dto.rating,
                    name = dto.name,
                    proximity = 0f,
                    imageLink = dto.imagePath
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
        type: String,
        category: String,
        sights: List<Int>
    ): Boolean {
        var resp = tourAPI.createTour(
            image = MultipartBody.Part.createFormData(
                name = "image",
                filename = "image.jpg",
                body = image.asRequestBody()
            ),
            tourJSON = MultipartBody.Part.createFormData(
                name = "tour", value = Gson().toJson(
                    CreateTourDTO(
                        name = name,
                        description = description,
                        city = city,
                        type = type,
                        category = category,
                        sights = sights
                    )
                )
            )
        ).execute()
        return resp.isSuccessful
    }

    suspend fun update(
        image: File?,
        tourId: Int,
        name: String,
        description: String,
        city: String,
        type: String,
        category: String
    ): Boolean {
        var resp: Response<String>
        var updateTourJson = MultipartBody.Part.createFormData(
            name = "tour", value = Gson().toJson(
                UpdateTourDTO(
                    id = tourId,
                    name = name,
                    description = description,
                    city = city,
                    type = type,
                    category = category
                )
            )
        )
        if (image == null) {
            resp = tourAPI.updateTourWithoutImage(
                updateTourJSON = updateTourJson
            ).execute()
        } else {
            resp = tourAPI.updateTourWithImage(
                image = MultipartBody.Part.createFormData(
                    name = "image",
                    filename = "image.jpg",
                    body = image.asRequestBody()
                ),
                updateTourJSON = updateTourJson
            ).execute()
        }

        return resp.isSuccessful
    }

    suspend fun delete(
        tourId: Int
    ): Boolean {
        var resp = tourAPI.deleteTour(
            tourId
        ).execute()
        return resp.isSuccessful
    }
}