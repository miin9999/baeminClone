package aop.fastcampus.part06.chapter01.model.restaurant.food

import aop.fastcampus.part06.chapter01.data.entity.RestaurantFoodEntity
import aop.fastcampus.part06.chapter01.model.CellType
import aop.fastcampus.part06.chapter01.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val foodId: String,
    val restaurantTitle: String
) : Model(id, type) {

    fun toEntity(basketIndex: Int) = RestaurantFoodEntity(
        "${foodId}_${basketIndex}", title, description, price, imageUrl, restaurantId, restaurantTitle
    )

}