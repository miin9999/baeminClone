package aop.fastcampus.part06.chapter01.data.repository.restaurant.review

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String) : DefaultRestaurantReviewRepository.Result
}