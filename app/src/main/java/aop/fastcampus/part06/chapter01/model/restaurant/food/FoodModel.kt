package aop.fastcampus.part06.chapter01.model.restaurant.food

import aop.fastcampus.part06.chapter01.data.entity.RestaurantFoodEntity
import aop.fastcampus.part06.chapter01.model.CellType
import aop.fastcampus.part06.chapter01.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType =CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val foodId: String
): Model(id, type){

    // basketIndex는 한 메뉴를 여러번 담을 수 있게 하기 위함
    fun toEntity(basketIndex: Int) = RestaurantFoodEntity(
        "${foodId}_${basketIndex}", title, description, price, imageUrl, restaurantId
    )


}