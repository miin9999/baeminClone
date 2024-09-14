package aop.fastcampus.part06.chapter01.util.mapper

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import aop.fastcampus.part06.chapter01.databinding.ViewholderEmptyBinding
import aop.fastcampus.part06.chapter01.databinding.ViewholderRestaurantBinding
import aop.fastcampus.part06.chapter01.model.CellType
import aop.fastcampus.part06.chapter01.model.Model
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import aop.fastcampus.part06.chapter01.util.provider.ResourcesProvider
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.EmptyViewHolder
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.ModelViewHolder
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.restaurant.RestaurantViewHolder

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
        }
        return viewHolder as ModelViewHolder<M>

    }
}