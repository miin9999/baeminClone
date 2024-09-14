package aop.fastcampus.part06.chapter01.data.repository.restaurant

import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.entity.RestaurantEntity
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantCategory

interface RestaurantRepository {

    suspend fun getList(
        restaurantCategory:RestaurantCategory,
        locationLatLngEntity: LocationLatLngEntity
    ):List<RestaurantEntity>
}