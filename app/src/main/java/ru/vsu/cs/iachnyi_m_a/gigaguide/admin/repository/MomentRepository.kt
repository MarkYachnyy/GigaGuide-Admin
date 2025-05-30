package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository

import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.api.MomentAPI
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.ServerResponseMessageDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.moment.CreateMomentDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.moment.MomentDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.moment.UpdateMomentDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.MomentInfo
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.dto.mapper.MomentDTOMapper
import java.io.File

class MomentRepository {

    private val momentAPI =
        Retrofit.Builder().baseUrl("${ServerUtils.SERVER_ADDRESS}/api/tour-sight/")
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

    suspend fun createMoment(
        image: File,
        name: String,
        sightId: Int,
        content: String,
        orderNumber: Int,
        latitude: Double,
        longitude: Double
    ): Boolean? {
        var resp = momentAPI.createMoment(
            image = MultipartBody.Part.createFormData(
                name = "image",
                filename = "image.jpg",
                body = image.asRequestBody()
            ),
            momentJSON = MultipartBody.Part.createFormData(
                name = "moment", value = Gson().toJson(
                    CreateMomentDTO(
                        name = name,
                        content = content,
                        orderNumber = orderNumber,
                        sightId = sightId,
                        latitude = latitude,
                        longitude = longitude
                    )
                )
            )
        ).execute()
        return resp.isSuccessful
    }

    suspend fun updateMoment(
        image: File?,
        id: Int,
        name: String,
        content: String,
        orderNumber: Int,
        latitude: Double,
        longitude: Double
    ): Boolean? {
        var updateMomentJSON = Gson().toJson(
            UpdateMomentDTO(
                id = id,
                name = name,
                content = content,
                orderNumber = orderNumber,
                latitude = latitude,
                longitude = longitude
            )
        )
        var resp: Response<ServerResponseMessageDTO>
        if (image == null) {
            resp = momentAPI.updateMomentWithoutImage(
                updateMomentJSON = MultipartBody.Part.createFormData(
                    name = "moment", value = updateMomentJSON
                )
            ).execute()
        } else {
            resp = momentAPI.updateMomentWithImage(
                image = MultipartBody.Part.createFormData(
                    name = "image",
                    filename = "image.jpg",
                    body = image.asRequestBody()
                ),
                updateMomentJSON = MultipartBody.Part.createFormData(
                    name = "moment", value = updateMomentJSON
                )
            ).execute()
        }

        return resp.isSuccessful
    }

    suspend fun deleteMoment(
        id: Int
    ): Boolean? {
        var resp: Response<ServerResponseMessageDTO> = momentAPI.deleteMoment(id).execute()
        return resp.isSuccessful
    }

}