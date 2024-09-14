package aop.fastcampus.part06.chapter01.widget.adapter.viewholder

import aop.fastcampus.part06.chapter01.databinding.ViewholderEmptyBinding
import aop.fastcampus.part06.chapter01.model.Model
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import aop.fastcampus.part06.chapter01.util.provider.ResourcesProvider
import aop.fastcampus.part06.chapter01.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    private val binding:ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<Model>(binding,viewModel,resourcesProvider) {
    override fun reset() = Unit

    override fun bindViews(model: Model, adapterListener: AdapterListener) = Unit


}