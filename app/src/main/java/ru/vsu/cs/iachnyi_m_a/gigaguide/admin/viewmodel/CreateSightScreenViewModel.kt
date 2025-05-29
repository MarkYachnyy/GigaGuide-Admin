package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel

import android.content.Context
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
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.SightRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import java.io.File

class CreateSightScreenViewModel(
    private val sightRepository: SightRepository,
    private val activity: ComponentActivity
) : ViewModel() {

    private var pickImage = activity.registerForActivityResult(PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            sightImageURI = uri
        } else {
            Log.e("PhotoPicker", "No media selected")
        }
    }

    var sightImageURI by mutableStateOf<Uri?>(null)
    var sightName by mutableStateOf("")
    var sightDescription by mutableStateOf("")
    var sightLatitude by mutableDoubleStateOf(0.0)
    var sightLongitude by mutableDoubleStateOf(0.0)
    var sightCity by mutableStateOf("")
    var error by mutableStateOf("")

    fun createSight(successCallback: () -> Unit) {
        if (sightName.trim().isEmpty()) {
            error = "Не указано имя"
        } else if (sightCity.trim().isEmpty()) {
            error = "Не указан город"
        } else if (sightDescription.trim().isEmpty()) {
            error = "Не указано описание"
        } else if (sightImageURI == null || sightImageURI.toString() == "") {
            error = "Не указано изображение"
        } else {
            error = ""
            viewModelScope.launch {
                var file = sightImageURI!!.toFile(activity)
                if(file == null){
                    error = "Ошибка открытия файла"
                    return@launch
                }
                var success = ServerUtils.executeNetworkCall {
                    sightRepository.create(
                        image = file,
                        name = sightName,
                        description = sightDescription,
                        city = sightCity,
                        latitude = sightLatitude,
                        longitude = sightLongitude
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