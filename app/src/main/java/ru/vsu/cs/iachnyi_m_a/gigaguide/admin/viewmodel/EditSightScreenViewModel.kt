package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightInfo
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.MapRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.MomentRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.SightRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils

class EditSightScreenViewModel(
    private val sightRepository: SightRepository,
    private val momentRepository: MomentRepository,
    private val mapRepository: MapRepository,
    private val activity: ComponentActivity
    ) : ViewModel() {

    private var pickImage = activity.registerForActivityResult(PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            newSightImageURI = uri
        } else {
            Log.e("PhotoPicker", "No media selected")
        }
    }

    var sight: SightInfo? = null

    var newSightImageURI by mutableStateOf<Uri?>(null)
    var newSightName by mutableStateOf("")
    var newSightDescription by mutableStateOf("")
    var newSightLatitude by mutableDoubleStateOf(0.0)
    var newSightLongitude by mutableDoubleStateOf(0.0)
    var newSightCity by mutableStateOf("")
    var error by mutableStateOf("")

    fun loadExistingSight(id: Int){
        viewModelScope.launch {
            var loaded = ServerUtils.executeNetworkCall { sightRepository.getSightInfoById(id.toLong()) }
            if(loaded != null) {
                sight = loaded
                newSightName = loaded.name
                newSightDescription = loaded.description
                newSightCity = loaded.city
                var coords = ServerUtils.executeNetworkCall { mapRepository.getCoordinatedOfSight(id.toLong()) }
                if(coords != null){
                    newSightLatitude = coords.latitude
                    newSightLongitude = coords.longitude
                }
            }
        }
    }

    fun updateSight(successCallback: () -> Unit) {
        if (newSightName.trim().isEmpty()) {
            error = "Не указано имя"
        } else if (newSightCity.trim().isEmpty()) {
            error = "Не указан город"
        } else if (newSightDescription.trim().isEmpty()) {
            error = "Не указано описание"
        } else if (newSightImageURI == null || newSightImageURI.toString() == "") {
            error = "Не указано изображение"
        } else {
            error = ""
            viewModelScope.launch {
                var file = newSightImageURI!!.toFile(activity)
                if(file == null){
                    error = "Ошибка открытия файла"
                    return@launch
                }
                var success = ServerUtils.executeNetworkCall {
                    sightRepository.create(
                        image = file,
                        name = newSightName,
                        description = newSightDescription,
                        city = newSightCity,
                        latitude = newSightLatitude,
                        longitude = newSightLongitude
                    )
                }
                if(success != null && success){
                    successCallback.invoke()
                }
            }
        }
    }

    fun uploadSightPhoto() {
        pickImage.launch(PickVisualMediaRequest(PickVisualMedia.SingleMimeType("image/jpeg")))
    }

}