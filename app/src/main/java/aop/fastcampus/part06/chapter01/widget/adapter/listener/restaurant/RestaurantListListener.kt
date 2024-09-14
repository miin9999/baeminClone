package aop.fastcampus.part06.chapter01.widget.adapter.listener.restaurant

import aop.fastcampus.part06.chapter01.model.restaurant.RestaurantModel
import aop.fastcampus.part06.chapter01.widget.adapter.listener.AdapterListener

interface RestaurantListListener: AdapterListener{

    fun onClickItem(model: RestaurantModel)
}