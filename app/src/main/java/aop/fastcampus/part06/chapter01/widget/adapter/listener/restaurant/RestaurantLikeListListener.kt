package aop.fastcampus.part06.chapter01.widget.adapter.listener.restaurant

import aop.fastcampus.part06.chapter01.model.restaurant.RestaurantModel

interface RestaurantLikeListListener: RestaurantListListener {

    // 찜 해제 로직
    fun onDislikeItem(model: RestaurantModel)

}