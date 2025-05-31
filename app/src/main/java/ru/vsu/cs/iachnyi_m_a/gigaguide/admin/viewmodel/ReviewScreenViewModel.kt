package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.datastore.DataStoreManager
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.ReviewRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.SightReviewRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.repository.TourReviewRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.model.review.Review

class ReviewScreenViewModel(
    private val sightReviewRepository: SightReviewRepository,
    private val tourReviewRepository: TourReviewRepository,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    var objectId = -1
    var isTour = false
    var otherReviews = mutableStateListOf<Review>()

    fun loadReviews() {
        var repository: ReviewRepository =
            if (isTour) tourReviewRepository else sightReviewRepository
        viewModelScope.launch {
            var token = dataStoreManager.getJWT()
            if(token == null) return@launch
            var reviews =
                ServerUtils.executeNetworkCall { repository.getAllReviews(objectId) }
            if (reviews != null) {
                otherReviews.clear()
                otherReviews.addAll(reviews.otherReviews)
            }
        }
    }

    fun deleteReview(id: Int) {
        viewModelScope.launch {
            var repository: ReviewRepository =
                if (isTour) tourReviewRepository else sightReviewRepository
            if (dataStoreManager.getJWT() == null) return@launch
            var success =
                ServerUtils.executeNetworkCall {
                    repository.deleteReview(
                        token = dataStoreManager.getJWT()!!,
                        id = id
                    )
                }
            if(success != null) {
                loadReviews()
            }
        }
    }
}