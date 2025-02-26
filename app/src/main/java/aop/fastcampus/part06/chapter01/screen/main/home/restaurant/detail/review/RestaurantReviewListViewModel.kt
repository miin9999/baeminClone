package aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aop.fastcampus.part06.chapter01.data.entity.ReviewEntity
import aop.fastcampus.part06.chapter01.data.repository.restaurant.review.DefaultRestaurantReviewRepository
import aop.fastcampus.part06.chapter01.data.repository.restaurant.review.RestaurantReviewRepository
import aop.fastcampus.part06.chapter01.model.restaurant.review.RestaurantReviewModel
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantReviewRepository: RestaurantReviewRepository,
) :BaseViewModel(){

    val reviewStateLiveData = MutableLiveData<RestaurantReviewState>(RestaurantReviewState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        reviewStateLiveData.value = RestaurantReviewState.Loading
        when(val result = restaurantReviewRepository.getReviews(restaurantTitle)){
            is DefaultRestaurantReviewRepository.Result.Success<*> ->{
                val reviews = result.data as List<ReviewEntity>
                reviewStateLiveData.value = RestaurantReviewState.Success(
                    reviews.map{
                        RestaurantReviewModel(
                            id = it.hashCode().toLong(),
                            title = it.title,
                            description = it.content,
                            grade = it.rating,
                            thumbnailImageUri = if(it.imageUrlList.isNullOrEmpty()){
                                null
                            } else{
                                Uri.parse(it.imageUrlList.first())
                            }

                        )
                    }
                )
            }

            else -> Unit
        }

    }
}