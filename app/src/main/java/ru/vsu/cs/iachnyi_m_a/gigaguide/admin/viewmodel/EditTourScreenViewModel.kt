package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.TourInfo
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.SightRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.TourRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils

class EditTourScreenViewModel(
    private val sightRepository: SightRepository,
    private val tourRepository: TourRepository,
    private val activity: ComponentActivity
) : ViewModel() {

    var imagePickedCallback: (Uri) -> Unit = {}
    private var pickImage = activity.registerForActivityResult(PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            imagePickedCallback.invoke(uri)
        } else {
            Log.e("PhotoPicker", "No media selected")
        }
    }

    var tour: TourInfo? = null

    var newTourImageURI by mutableStateOf<Uri?>(null)
    var newTourName by mutableStateOf("")
    var newTourDescription by mutableStateOf("")
    var newTourCategory by mutableStateOf("")
    var newTourType by mutableStateOf("")
    var updateTourError by mutableStateOf("")

    fun loadExistingTour(id: Int) {
        viewModelScope.launch {
            var loaded =
                ServerUtils.executeNetworkCall { tourRepository.getTourInfoById(id.toLong()) }
            if (loaded != null) {
                newTourImageURI = null
                tour = loaded
                newTourName = loaded.name
                newTourDescription = loaded.description
                newTourType = loaded.type
                newTourCategory = loaded.category
            }
        }
    }
    fun updateTour() {
        if (newTourName.trim().isEmpty()) {
            updateTourError = "Не указано имя"
        } else if (newTourCategory.trim().isEmpty()) {
            updateTourError = "Не указана категория"
        } else if (newTourType.trim().isEmpty()) {
            updateTourError = "Не указан тип"
        } else if (newTourDescription.trim().isEmpty()) {
            updateTourError = "Не указано описание"
        } else {
            updateTourError = ""
            viewModelScope.launch {
                var sight1 = ServerUtils.executeNetworkCall { sightRepository.getSightInfoById(tour!!.sights[0].sightId) }
                if(sight1 == null){
                    updateTourError = "Ошибка обновления тура"
                    return@launch
                }
                var file =
                    if (newTourImageURI == null) null else newTourImageURI!!.toFile(activity)
                var success = ServerUtils.executeNetworkCall {
                    tourRepository.update(
                        tourId = tour!!.id.toInt(),
                        image = file,
                        name = newTourName,
                        description = newTourDescription,
                        city = sight1.city,
                        category = newTourCategory,
                        type = newTourType
                    )
                }
                if (success != null && success) {
                    loadExistingTour(
                        tour!!.id.toInt()
                    )
                }
            }
        }
    }

    fun uploadTourPhoto() {
        imagePickedCallback = { newTourImageURI = it }
        pickImage.launch(PickVisualMediaRequest(PickVisualMedia.SingleMimeType("image/jpeg")))
    }

    fun deleteTour(success: () -> Unit){
        viewModelScope.launch {
            var resp = ServerUtils.executeNetworkCall { tourRepository.delete(tour!!.id.toInt()) }
            if(resp != null && resp){
                success.invoke()
            }
        }
    }

}