package aop.fastcampus.part06.chapter01.screen.order

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import aop.fastcampus.part06.chapter01.databinding.ActivityOrderMenuListBinding
import aop.fastcampus.part06.chapter01.model.restaurant.food.FoodModel
import aop.fastcampus.part06.chapter01.screen.base.BaseActivity
import aop.fastcampus.part06.chapter01.util.provider.ResourcesProvider
import aop.fastcampus.part06.chapter01.widget.adapter.ModelRecyclerAdapter
import aop.fastcampus.part06.chapter01.widget.adapter.listener.order.OrderMenuListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel



class OrderMenuListActivity : BaseActivity<OrderMenuListViewModel, ActivityOrderMenuListBinding>() {

    override fun getViewBinding(): ActivityOrderMenuListBinding =
        ActivityOrderMenuListBinding.inflate(layoutInflater)

    override val viewModel by viewModel<OrderMenuListViewModel>()

    private val resourcesProvider: ResourcesProvider by inject<ResourcesProvider>()

    companion object {
        fun newIntent(context: Context) = Intent(context, OrderMenuListActivity::class.java)
    }




    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, OrderMenuListViewModel>(listOf(), viewModel, resourcesProvider,adapterListener = object :
            OrderMenuListListener {
            override fun onRemoveItem(model: FoodModel) {
                viewModel.removeOrderMenu(model)
            }
        })
    }


    override fun initViews() = with(binding) {

        recyclerView.adapter = adapter

        toolbar.setNavigationOnClickListener { finish() }
        confirmButton.setOnClickListener{
            viewModel.orderMenu()
        }

        orderClearButton.setOnClickListener{
            viewModel.clearOrderMenu()
        }

    }


    override fun observeData() = viewModel.orderMenuStateLiveData.observe(this){
        when(it){
            is OrderMenuState.Loading ->{
                handleLoading()

            }
            is OrderMenuState.Success ->{
                handleSuccess(it)
            }
            is OrderMenuState.Order ->{
                handleOrderState()

            }
            is OrderMenuState.Error ->{
                handleErrorState(it)

            }

            else -> Unit



        }
    }


    private fun handleLoading() = with(binding){
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: OrderMenuState.Success) = with(binding){
        progressBar.isGone = true
        adapter.submitList(state.restaurantFoodModelList)
        val menuOrderIsEmpty = state.restaurantFoodModelList.isNullOrEmpty()
        confirmButton.isEnabled = menuOrderIsEmpty.not()
        if(menuOrderIsEmpty){
            Toast.makeText(this@OrderMenuListActivity,"주문 메뉴가 없어 화면을 종료합니다.",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun handleOrderState(){
        Toast.makeText(this,"성공적으로 주문을 완료하였습니다.",Toast.LENGTH_SHORT).show()
        finish()

    }

    private fun handleErrorState(state: OrderMenuState.Error){
        Toast.makeText(this,getString(state.messageId,state.e),Toast.LENGTH_SHORT).show()

    }

}