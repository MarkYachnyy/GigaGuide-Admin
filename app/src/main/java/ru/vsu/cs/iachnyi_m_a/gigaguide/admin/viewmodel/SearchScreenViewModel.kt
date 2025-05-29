package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel

import android.util.Log
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

class SearchScreenViewModel (private val sightRepository: SightRepository,
    private val tourRepository: TourRepository): ViewModel() {

    var sightResult = mutableStateListOf<SightTourThumbnail>()
    var tourResult = mutableStateListOf<SightTourThumbnail>()
    var loading by mutableStateOf(false)
    var searchBarValue by mutableStateOf("")
    var searchTours by mutableStateOf(false)

    fun loadSearchResult(){

        loading = true

        viewModelScope.launch {
            sightResult.clear()
            tourResult.clear()

            if(!searchTours){
                var sightInfos: List<SightSearchResult>? = ServerUtils.executeNetworkCall { sightRepository.search(searchBarValue.trim()) }
                if (sightInfos != null) {
                    sightResult.addAll(sightInfos.map { SightTourThumbnail(sightId = it.id.toLong(), name = it.name, rating = it.rating, proximity = 0f, imageLink = it.imageLink) });
                }
            } else {
                var tourInfos: List<SightTourThumbnail>? = ServerUtils.executeNetworkCall { tourRepository.searchTours(searchBarValue.trim()) }
                if (tourInfos != null) {
                    tourResult.addAll(tourInfos.map { ti ->
                        SightTourThumbnail(
                            sightId = ti.sightId,
                            name = ti.name,
                            rating = ti.rating,
                            proximity = 0f,
                            imageLink = ti.imageLink
                        )
                    });
                }
            }

            loading = false
        }
    }
}