package aop.fastcampus.part06.chapter01.widget.adapter.viewholder.review

import androidx.core.view.isGone
import androidx.core.view.isVisible
import aop.fastcampus.part06.chapter01.databinding.ViewholderRestaurantReviewBinding
import aop.fastcampus.part06.chapter01.extensions.clear
import aop.fastcampus.part06.chapter01.extensions.load
import aop.fastcampus.part06.chapter01.model.restaurant.review.RestaurantReviewModel
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import aop.fastcampus.part06.chapter01.util.provider.ResourcesProvider
import aop.fastcampus.part06.chapter01.widget.adapter.listener.AdapterListener
import aop.fastcampus.part06.chapter01.widget.adapter.viewholder.ModelViewHolder

class RestaurantReviewHolder(
    private val binding: ViewholderRestaurantReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<RestaurantReviewModel>(binding,viewModel,resourcesProvider) {

    override fun reset() = with(binding){
        reviewThumbnailImage.clear()
        reviewThumbnailImage.isGone = true
    }



    override fun bindData(model: RestaurantReviewModel) {
        super.bindData(model)
        with(binding) {
            if (model.thumbnailImageUri != null) {
                reviewThumbnailImage.isVisible = true
                reviewThumbnailImage.load(model.thumbnailImageUri.toString())
            } else {
                reviewThumbnailImage.isGone = true
            }

            reviewTitleText.text = model.title
            reviewText.text = model.description
            ratingBar.rating = model.grade


        }
    }

    override fun bindViews(model: RestaurantReviewModel, adapterListener: AdapterListener) = Unit

}