package aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.review

import aop.fastcampus.part06.chapter01.model.restaurant.review.RestaurantReviewModel

sealed class RestaurantReviewState {
    object Uninitialized: RestaurantReviewState()

    object Loading: RestaurantReviewState()

    data class Success(
        val reviewList: List<RestaurantReviewModel>

    ):RestaurantReviewState()


}