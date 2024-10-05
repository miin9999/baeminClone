package aop.fastcampus.part06.chapter01.widget.adapter.listener.restaurant

import aop.fastcampus.part06.chapter01.model.restaurant.food.FoodModel
import aop.fastcampus.part06.chapter01.widget.adapter.listener.AdapterListener

interface FoodMenuListListener: AdapterListener {

    fun onClickItem(model: FoodModel)
}