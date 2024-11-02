package aop.fastcampus.part06.chapter01.widget.adapter.listener.order

import aop.fastcampus.part06.chapter01.model.restaurant.food.FoodModel
import aop.fastcampus.part06.chapter01.widget.adapter.listener.AdapterListener

interface OrderMenuListListener: AdapterListener {

    fun onRemoveItem(model: FoodModel)
}