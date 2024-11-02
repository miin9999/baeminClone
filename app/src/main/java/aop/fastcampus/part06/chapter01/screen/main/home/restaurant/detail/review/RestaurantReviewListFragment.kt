package aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.review

import androidx.core.os.bundleOf
import aop.fastcampus.part06.chapter01.databinding.FragmentListBinding
import aop.fastcampus.part06.chapter01.model.restaurant.review.RestaurantReviewModel
import aop.fastcampus.part06.chapter01.screen.base.BaseFragment
import aop.fastcampus.part06.chapter01.util.provider.ResourcesProvider
import aop.fastcampus.part06.chapter01.widget.adapter.ModelRecyclerAdapter
import aop.fastcampus.part06.chapter01.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantReviewListFragment:BaseFragment<RestaurantReviewListViewModel,FragmentListBinding> (){

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)


    override val viewModel by viewModel<RestaurantReviewListViewModel>{
        parametersOf(
            arguments?.getString(RESTAURANT_TITLE_KEY)
        )
    }

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy{
        ModelRecyclerAdapter<RestaurantReviewModel, RestaurantReviewListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object: AdapterListener{}
        )
    }

    override fun initViews() {
        binding.recyclerView.adapter = adapter
    }



    override fun observeData() = viewModel.reviewStateLiveData.observe(viewLifecycleOwner) {
        when(it){
            is RestaurantReviewState.Success ->{
                handleSuccess(it)
            }
            else -> null
        }


    }

    private fun handleSuccess(state: RestaurantReviewState.Success){
        adapter.submitList(state.reviewList)
    }



    companion object{

        const val RESTAURANT_TITLE_KEY = "restaurantTitle"

        fun newInstance(restaurantTitle: String):RestaurantReviewListFragment{
            val bundle = bundleOf(
                RESTAURANT_TITLE_KEY to restaurantTitle
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }


}