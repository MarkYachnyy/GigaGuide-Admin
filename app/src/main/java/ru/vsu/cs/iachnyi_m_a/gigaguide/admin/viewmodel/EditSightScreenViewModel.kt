package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.EditMoment
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

    var sight: SightInfo? = null

    var newSightImageURI by mutableStateOf<Uri?>(null)
    var newSightName by mutableStateOf("")
    var newSightDescription by mutableStateOf("")
    var newSightLatitude by mutableDoubleStateOf(0.0)
    var newSightLongitude by mutableDoubleStateOf(0.0)
    var newSightCity by mutableStateOf("")
    var updateSightError by mutableStateOf("")
    var updateMomentsError by mutableStateOf("")

    var editMoments = mutableStateListOf<EditMoment>()

    fun loadExistingSight(id: Int) {
        viewModelScope.launch {
            var loaded =
                ServerUtils.executeNetworkCall { sightRepository.getSightInfoById(id.toLong()) }
            if (loaded != null) {
                newSightImageURI = null
                sight = loaded
                newSightName = loaded.name
                newSightDescription = loaded.description
                newSightCity = loaded.city
                var coords =
                    ServerUtils.executeNetworkCall { mapRepository.getCoordinatedOfSight(id.toLong()) }
                if (coords != null) {
                    newSightLatitude = coords.latitude
                    newSightLongitude = coords.longitude
                }
            }
        }
    }

    fun loadExistingMoments(sightId: Int) {
        viewModelScope.launch {
            editMoments.clear()

            var loadedMoments =
                ServerUtils.executeNetworkCall { momentRepository.getSightMoments(sightId.toLong()) }
            if (loadedMoments != null) {
                for (moment in loadedMoments) {
                    var momentCoords =
                        ServerUtils.executeNetworkCall { mapRepository.getCoordinatesOfMoment(moment.id) }
                    if (momentCoords != null) {
                        var editMoment = EditMoment()
                        editMoment.existingMoment = moment
                        editMoment.newMomentName = moment.name
                        editMoment.newMomentContent = ""
                        editMoment.newMomentLatitude = momentCoords.latitude
                        editMoment.newMomentLongitude = momentCoords.longitude
                        editMoments.add(editMoment)
                    }
                }
            }

        }
    }

    fun updateSight() {
        if (newSightName.trim().isEmpty()) {
            updateSightError = "Не указано имя"
        } else if (newSightCity.trim().isEmpty()) {
            updateSightError = "Не указан город"
        } else if (newSightDescription.trim().isEmpty()) {
            updateSightError = "Не указано описание"
        } else {
            updateSightError = ""
            viewModelScope.launch {
                var file =
                    if (newSightImageURI == null) null else newSightImageURI!!.toFile(activity)
                var success = ServerUtils.executeNetworkCall {
                    sightRepository.update(
                        image = file,
                        sightId = sight!!.id.toInt(),
                        name = newSightName,
                        description = newSightDescription,
                        city = newSightCity,
                        latitude = newSightLatitude,
                        longitude = newSightLongitude
                    )
                }
                if (success != null && success) {
                    loadExistingSight(
                        sight!!.id.toInt()
                    )
                }
            }
        }
    }

    fun updateMoments() {
        for (editMoment in editMoments) {
            if (!validateEditMoment(editMoment)) {
                updateMomentsError = "Заполнены не все поля"
                return
            }
        }
        var newIndex = 0
        var totalToChange = 0;
        for (editMoment in editMoments) {
            if (!editMoment.willDelete) newIndex++
            if (willEditMomentChangeAnything(editMoment, newIndex)) {
                totalToChange++
            }
        }
        if (totalToChange == 0) {
            updateMomentsError = "Не внесено никаких изменений в моменты"
            return
        }
        newIndex = 0
        viewModelScope.launch {
            for (editMoment in editMoments) {
                if(!editMoment.willDelete) newIndex++
                if(!willEditMomentChangeAnything(editMoment, newIndex)) continue
                if (editMoment.willDelete) {
                    var resp =
                        ServerUtils.executeNetworkCall { momentRepository.deleteMoment(editMoment.existingMoment!!.id.toInt()) }
                    if (resp == null || !resp) {
                        updateMomentsError = "Ошибка обновления моментов"
                        break
                    }
                } else if (editMoment.existingMoment == null) {
                    var resp = ServerUtils.executeNetworkCall {
                        momentRepository.createMoment(
                            image = editMoment.newMomentImageURI!!.toFile(activity)!!,
                            name = editMoment.newMomentName,
                            sightId = sight!!.id.toInt(),
                            latitude = editMoment.newMomentLatitude,
                            longitude = editMoment.newMomentLongitude,
                            content = editMoment.newMomentContent,
                            orderNumber = newIndex
                        )
                    }
                    if (resp == null || !resp) {
                        updateMomentsError = "Ошибка обновления моментов"
                        break
                    }
                } else {
                    var resp = ServerUtils.executeNetworkCall {
                        momentRepository.updateMoment(
                            id = editMoment.existingMoment!!.id.toInt(),
                            image = if(editMoment.newMomentImageURI == null) null else editMoment.newMomentImageURI!!.toFile(activity)!!,
                            name = editMoment.newMomentName,
                            latitude = editMoment.newMomentLatitude,
                            longitude = editMoment.newMomentLongitude,
                            content = editMoment.newMomentContent,
                            orderNumber = newIndex
                        )
                    }
                    if (resp == null || !resp) {
                        updateMomentsError = "Ошибка обновления моментов"
                        break
                    }
                }
            }
            loadExistingMoments(sight!!.id.toInt())
        }
    }

    fun validateEditMoment(editMoment: EditMoment): Boolean {
        if (editMoment.willDelete) return true
        if (editMoment.newMomentName.trim() == "") return false
        if (editMoment.newMomentContent.trim() == "") return false
        if (editMoment.existingMoment == null && editMoment.newMomentImageURI == null) return false
        return true
    }

    fun willEditMomentChangeAnything(editMoment: EditMoment, newMomentIndex: Int): Boolean {
        if (editMoment.willDelete) return true
        if (editMoment.existingMoment == null) return true
        if(editMoment.existingMomentCoordinates == null) return true
        if (editMoment.newMomentName != editMoment.existingMoment!!.name.trim()) return true
        if (editMoment.newMomentLatitude != editMoment.existingMomentCoordinates!!.latitude) return true
        if (editMoment.newMomentLongitude != editMoment.existingMomentCoordinates!!.longitude) return true
        if (editMoment.newMomentImageURI != null) return true
        if (newMomentIndex != editMoment.existingMoment!!.orderNumber) return true
        return false
    }

    fun uploadSightPhoto() {
        imagePickedCallback = { newSightImageURI = it }
        pickImage.launch(PickVisualMediaRequest(PickVisualMedia.SingleMimeType("image/jpeg")))
    }

    fun uploadMomentPhoto(i: Int) {
        imagePickedCallback = { editMoments[i].newMomentImageURI = it }
        pickImage.launch(PickVisualMediaRequest(PickVisualMedia.SingleMimeType("image/jpeg")))
    }

    fun addEmptyMoment() {
        var editMoment: EditMoment = EditMoment()
        editMoments.add(editMoment)
    }

    fun deleteSight(success: () -> Unit){
        viewModelScope.launch {
            var resp = ServerUtils.executeNetworkCall { sightRepository.delete(sight!!.id.toInt()) }
            if(resp != null && resp){
                success.invoke()
            }
        }
    }

}