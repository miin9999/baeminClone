package aop.fastcampus.part06.chapter01.data.response.restaurant

import aop.fastcampus.part06.chapter01.data.entity.RestaurantFoodEntity

data class RestaurantFoodResponse(

    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val imageUrl: String,

){

    fun toEntity(restaurantId: Long) = RestaurantFoodEntity(
        id, title, description, price.toDouble().toInt(), imageUrl, restaurantId
    )
}
