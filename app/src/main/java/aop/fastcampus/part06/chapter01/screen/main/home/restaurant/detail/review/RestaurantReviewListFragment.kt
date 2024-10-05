package aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.review

import androidx.core.os.bundleOf
import aop.fastcampus.part06.chapter01.data.entity.RestaurantFoodEntity
import aop.fastcampus.part06.chapter01.databinding.FragmentListBinding
import aop.fastcampus.part06.chapter01.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestaurantReviewListFragment:BaseFragment<RestaurantReviewListViewModel,FragmentListBinding> (){

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)


    override val viewModel by viewModel<RestaurantReviewListViewModel>()

    override fun observeData() {

    }

    companion object{

        const val RESTAURANT_ID_KEY = "restaurantId"

        fun newInstance(restaurantId: Long):RestaurantReviewListFragment{
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,

            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }


}