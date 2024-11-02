package aop.fastcampus.part06.chapter01.widget.adapter.viewholder.order

import aop.fastcampus.part06.chapter01.R
import aop.fastcampus.part06.chapter01.databinding.ViewholderOrderBinding
import aop.fastcampus.part06.chapter01.model.restaurant.order.OrderModel
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import aop.fastcampus.part06.chapter01.util.provider.ResourcesProvider
import aop.fastcampus.part06.chapter01.widget.adapter.listener.AdapterListener
import aop.fastcampus.part06.chapter01.widget.adapter.listener.order.OrderListListener
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.ModelViewHolder

class OrderViewHolder(
    private val binding: ViewholderOrderBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<OrderModel>(binding, viewModel, resourcesProvider) {


    override fun reset() = Unit

    override fun bindData(model: OrderModel) {
        super.bindData(model)
        with(binding) {
            orderTitleText.text = resourcesProvider.getString(R.string.order_history_title, model.orderId)

            val foodMenuList = model.foodMenuList

            foodMenuList
                .groupBy { it.title } // 같은 메뉴일 경우 하나로 합친다
                .entries.forEach{ (title,menuList) ->
                    val orderDataStr =
                        orderContentText.text.toString() + "메뉴: $title | 가격: ${menuList.first().price}원 X ${menuList.size}\n"
                    orderContentText.text = orderDataStr
                }
            orderContentText.text = orderContentText.text.trim()

            orderTotalPriceText.text =
                resourcesProvider.getString(
                    R.string.price,
                    foodMenuList.map{ it.price }.reduce {total, price -> total + price }

                )

        }
    }

    override fun bindViews(model: OrderModel, adapterListener: AdapterListener){
        if(adapterListener is OrderListListener){
            binding.root.setOnClickListener {
                adapterListener.writeRestaurantReview(model.orderId,model.restaurantTitle)

            }
        }
    }

}