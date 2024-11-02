package aop.fastcampus.part06.chapter01.screen.main.like

import androidx.core.view.isGone
import androidx.core.view.isVisible
import aop.fastcampus.part06.chapter01.databinding.FragmentRestaurantLikeListBinding
import aop.fastcampus.part06.chapter01.model.restaurant.RestaurantModel
import aop.fastcampus.part06.chapter01.screen.base.BaseFragment
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.RestaurantDetailActivity
import aop.fastcampus.part06.chapter01.util.provider.ResourcesProvider
import aop.fastcampus.part06.chapter01.widget.adapter.ModelRecyclerAdapter
import aop.fastcampus.part06.chapter01.widget.adapter.listener.restaurant.RestaurantLikeListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestaurantLikeListFragment: BaseFragment<RestaurantLikeListViewModel,FragmentRestaurantLikeListBinding>() {

    override fun getViewBinding(): FragmentRestaurantLikeListBinding =
        FragmentRestaurantLikeListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantLikeListViewModel>()

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantLikeListViewModel>(listOf(), viewModel,resourcesProvider,
            adapterListener = object : RestaurantLikeListListener {
            override fun onDislikeItem(model: RestaurantModel) {
                viewModel.dislikeRestaurant(model.toEntity())
            }

            override fun onClickItem(model: RestaurantModel) {
                startActivity(
                    RestaurantDetailActivity.newIntent(requireContext(), model.toEntity())
                )
            }
        })
    }

    override fun initViews() {
        binding.recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLivedata.observe(viewLifecycleOwner){
        checkListEmpty(it)
    }

    private fun checkListEmpty(restaurantList: List<RestaurantModel>){
        val isEmpty = restaurantList.isEmpty()
        binding.recyclerView.isGone = isEmpty
        binding.emptyResultTextView.isVisible = isEmpty
        if(isEmpty.not()){
            adapter.submitList(restaurantList)
        }
    }

    companion object {
        fun newInstance() = RestaurantLikeListFragment()

        const val TAG = "RestaurantLikeListFragment"
    }
}