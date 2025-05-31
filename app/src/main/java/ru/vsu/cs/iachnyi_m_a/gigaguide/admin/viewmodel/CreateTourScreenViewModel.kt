package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightSearchResult
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightTourThumbnail
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.SightRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.TourRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import java.io.File
import kotlin.toString

class CreateTourScreenViewModel(
    private val tourRepository: TourRepository,
    private val sightRepository: SightRepository,
    private val activity: ComponentActivity
) : ViewModel() {
    private var pickImage = activity.registerForActivityResult(PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            tourImageURI = uri
        } else {
            Log.e("PhotoPicker", "No media selected")
        }
    }

    var tourImageURI by mutableStateOf<Uri?>(null)
    var tourName by mutableStateOf("")
    var tourDescription by mutableStateOf("")
    var tourType by mutableStateOf("")
    var tourCategory by mutableStateOf("")
    var error by mutableStateOf("")
    var chosenSights = mutableStateListOf<SightTourThumbnail>()
    var searchResult = mutableStateListOf<SightTourThumbnail>()
    var searchQuery by mutableStateOf("")

    fun createTour(successCallback: () -> Unit) {
        if (tourName.trim().isEmpty()) {
            error = "Не указано имя"
        } else if (tourDescription.trim().isEmpty()) {
            error = "Не указано описание"
        } else if (tourCategory.trim().isEmpty()) {
            error = "Не указана категория"
        } else if (tourType.trim().isEmpty()) {
            error = "Не указан тип"
        } else if (chosenSights.size < 2) {
            error = "Выберите хотя бы две дост-ти"
        } else if (tourImageURI == null || tourImageURI.toString() == "") {
            error = "Не указано изображение"
        } else {
            error = ""
            viewModelScope.launch {
                var file = tourImageURI!!.toFile(activity)
                if(file == null){
                    error = "Ошибка открытия файла"
                    return@launch
                }
                var sight1 = ServerUtils.executeNetworkCall { sightRepository.getSightInfoById(chosenSights[0].sightId) }
                if(sight1 == null){
                    error = "Ошибка обновления тура"
                    return@launch
                }
                var success = ServerUtils.executeNetworkCall {
                    tourRepository.create(
                        image = file,
                        name = tourName,
                        description = tourDescription,
                        city = sight1.city,
                        type = tourType,
                        category = tourCategory,
                        sights = chosenSights.map { it.sightId.toInt() }
                    )
                }
                if(success != null && success){
                    successCallback.invoke()
                }
            }
        }
    }

    fun uploadPhoto() {
        pickImage.launch(PickVisualMediaRequest(PickVisualMedia.SingleMimeType("image/jpeg")))
    }

    fun searchSights(){
        searchResult.clear()

        viewModelScope.launch {
            var sightInfos: List<SightSearchResult>? = ServerUtils.executeNetworkCall { sightRepository.search(searchQuery.trim()) }
            if (sightInfos != null) {
                searchResult.addAll(sightInfos.map { SightTourThumbnail(sightId = it.id.toLong(), name = it.name, rating = it.rating, proximity = 0f, imageLink = it.imageLink) });
            }
        }

    }

    fun Uri.toFile(context: Context): File? {
        val inputStream = context.contentResolver.openInputStream(this)
        val tempFile = File.createTempFile("temp", ".jpg")
        return try {
            tempFile.outputStream().use { fileOut ->
                inputStream?.copyTo(fileOut)
            }
            tempFile.deleteOnExit()
            inputStream?.close()
            tempFile
        } catch (e: Exception) {
            null
        }
    }
}