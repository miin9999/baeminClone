package aop.fastcampus.part06.chapter01.data.repository.order

import aop.fastcampus.part06.chapter01.data.entity.RestaurantFoodEntity

interface OrderRepository {
    suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>,
        restaurantTitle: String
    ):DefaultOrderRepository.Result

    suspend fun getAllOrderMenus(
        userId:String
    ): DefaultOrderRepository.Result
}