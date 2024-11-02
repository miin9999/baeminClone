package aop.fastcampus.part06.chapter01.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import aop.fastcampus.part06.chapter01.databinding.ViewholderEmptyBinding
import aop.fastcampus.part06.chapter01.databinding.ViewholderFoodMenuBinding
import aop.fastcampus.part06.chapter01.databinding.ViewholderLikeRestaurantBinding
import aop.fastcampus.part06.chapter01.databinding.ViewholderOrderBinding
import aop.fastcampus.part06.chapter01.databinding.ViewholderOrderMenuBinding
import aop.fastcampus.part06.chapter01.databinding.ViewholderRestaurantBinding
import aop.fastcampus.part06.chapter01.databinding.ViewholderRestaurantReviewBinding
import aop.fastcampus.part06.chapter01.model.CellType
import aop.fastcampus.part06.chapter01.model.Model
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import aop.fastcampus.part06.chapter01.util.provider.ResourcesProvider
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.EmptyViewHolder
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.ModelViewHolder
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.food.FoodMenuViewHolder
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.order.OrderMenuViewHolder
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.order.OrderViewHolder
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.restaurant.LikeRestaurantViewHolder
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.restaurant.RestaurantViewHolder
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.review.RestaurantReviewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun<M: Model> map(
        parent: ViewGroup,
        type:CellType,
        viewModel:BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M>{

        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when(type){
            CellType.EMPTY_CELL ->EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )

            CellType.LIKE_RESTAURANT_CELL -> LikeRestaurantViewHolder(
                ViewholderLikeRestaurantBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )

            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )

            CellType.REVIEW_CELL -> RestaurantReviewHolder(
                ViewholderRestaurantReviewBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_FOOD_CELL-> OrderMenuViewHolder(
                ViewholderOrderMenuBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_CELL -> OrderViewHolder(
                ViewholderOrderBinding.inflate(inflater,parent,false),
                viewModel,
                resourcesProvider
            )


        }
        return viewHolder as ModelViewHolder<M>

    }
}