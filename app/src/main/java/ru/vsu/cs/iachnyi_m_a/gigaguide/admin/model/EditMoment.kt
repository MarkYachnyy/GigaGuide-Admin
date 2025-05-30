package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class EditMoment{
    var existingMoment: MomentInfo? = null
    var existingMomentCoordinates: MapPoint? = null

    var newMomentImageURI by mutableStateOf<Uri?>(null)
    var newMomentName: String by mutableStateOf("")
    var newMomentContent: String by mutableStateOf("")
    var newMomentLatitude by mutableDoubleStateOf(0.0)
    var newMomentLongitude by mutableDoubleStateOf(0.0)
    var willDelete by mutableStateOf(false)
}
