package aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail

import android.app.AlertDialog
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo.DetailedState
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import aop.fastcampus.part06.chapter01.R
import aop.fastcampus.part06.chapter01.data.entity.RestaurantEntity
import aop.fastcampus.part06.chapter01.data.entity.RestaurantFoodEntity
import aop.fastcampus.part06.chapter01.databinding.ActivityRestaurantDetailBinding
import aop.fastcampus.part06.chapter01.extensions.fromDpToPx
import aop.fastcampus.part06.chapter01.extensions.load
import aop.fastcampus.part06.chapter01.screen.base.BaseActivity
import aop.fastcampus.part06.chapter01.screen.main.MainTabMenu
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantListFragment
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.menu.RestaurantMenuListFragment
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail.review.RestaurantReviewListFragment
import aop.fastcampus.part06.chapter01.screen.order.OrderMenuListActivity
import aop.fastcampus.part06.chapter01.util.event.MenuChangeEventBus
import aop.fastcampus.part06.chapter01.widget.adapter.RestaurantDetailListFragmentPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class RestaurantDetailActivity : BaseActivity<RestaurantDetailViewModel,ActivityRestaurantDetailBinding>(

) {

    companion object{
        fun newIntent(context: Context, restaurantEntity:RestaurantEntity)
                = Intent(context,RestaurantDetailActivity::class.java).apply {
            putExtra(RestaurantListFragment.RESTAURANT_KEY,restaurantEntity)
        }

    }

    override fun getViewBinding(): ActivityRestaurantDetailBinding
        = ActivityRestaurantDetailBinding.inflate(layoutInflater)

    override val viewModel by viewModel<RestaurantDetailViewModel>{
        parametersOf(
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY)
        )
    }

    private val firebaseAuth by lazy{ FirebaseAuth.getInstance() }

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override fun initViews() {
        initAppBar()
    }


    private lateinit var viewPagerAdapter : RestaurantDetailListFragmentPagerAdapter


    private fun initAppBar() = with(binding){
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener{ appbarLayout,verticalOffset ->
            val topPadding = 300f.fromDpToPx().toFloat()
            val realAlphaScrollHeight = appbarLayout.measuredHeight - appbarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            val realAlphaVerticalOffset: Float = if(abstractOffset - topPadding<0) 0f else abstractOffset - topPadding

            if(abstractOffset<topPadding) {
                restaurantTitleTextView.alpha = 0f
                return@OnOffsetChangedListener
            }

            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            restaurantTitleTextView.alpha = 1 - (if(1-percentage *2 < 0) 0f else 1 - percentage*2)
        })

        toolbar.setNavigationOnClickListener { finish() }
        callButton.setOnClickListener {
            viewModel.getRestaurantTelNumber()?.let{ telNumber ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telNumber"))
                startActivity(intent)

            }

        }

        likeButton.setOnClickListener {
            viewModel.toggleLikedRestaurant()

        }

        shareButton.setOnClickListener {
            viewModel.getRestaurantInfo()?.let{ restaurantInfo->
                val intent = Intent(Intent.ACTION_SEND).apply{
                    type = MIMETYPE_TEXT_PLAIN
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "맛있는 음식점 : ${restaurantInfo.restaurantTitle}" +
                        "\n 평점 : ${restaurantInfo.grade}" +
                        "\n 연락처 : ${restaurantInfo.restaurantTelNumber}"
                    )
                    Intent.createChooser(this,"친구에게 공유하기")
                }
                startActivity(intent)

            }

        }

    }

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this){
        when(it){

            is RestaurantDetailState.Loading ->{
                handleLoading()

            }

            is RestaurantDetailState.Success ->{
                handleSuccess(it)
            }

            else -> Unit

        }
    }

    private fun handleLoading() = with(binding){
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {

        progressBar.isGone = true


        val restaurantEntity = state.restaurantEntity

        callButton.isGone = restaurantEntity.restaurantTelNumber ==null

        restaurantTitleTextView.text = restaurantEntity.restaurantTitle
        restaurantImage.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleTextView.text = restaurantEntity.restaurantTitle
        ratingBar.rating = restaurantEntity.grade
        deliveryTimeText.text =
            getString(R.string.delivery_expected_time, restaurantEntity.deliveryTimeRange.first, restaurantEntity.deliveryTimeRange.second)
        deliveryTipText.text =
            getString(R.string.delivery_tip_range, restaurantEntity.deliveryTipRange.first, restaurantEntity.deliveryTipRange.second)

        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this@RestaurantDetailActivity, if (state.isLiked == true) {
                R.drawable.ic_heart_enable
            } else {
                R.drawable.ic_heart_disable
            }),
            null, null, null
        )

        if(::viewPagerAdapter.isInitialized.not()){
            initViewPgaer(state.restaurantEntity.restaurantInfoId,state.restaurantEntity.restaurantTitle ,state.restaurantFoodList)

        }

        notifyBasketCount(state.foodMenuListInBasket)

        val (isClearNeed,afterAction) = state.isClearNeedInBasketAndAction
        if(isClearNeed){
            alertClearNeedInBasket(afterAction)

        }

    }



    private fun initViewPgaer(
        restaurantInfoId: Long,
        restaurantTitle: String,
        restaurantFoodList: List<RestaurantFoodEntity>?
    ) {
        viewPagerAdapter = RestaurantDetailListFragmentPagerAdapter(
            this,
            listOf(
                RestaurantMenuListFragment.newInstance(
                    restaurantInfoId, ArrayList(restaurantFoodList ?: listOf())
                ),
                RestaurantReviewListFragment.newInstance(
                    restaurantTitle
                ),


            )
        )
        binding.menuAndReviewViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.menuAndReviewTabLayout, binding.menuAndReviewViewPager) { tab, position->
            tab.setText(RestaurantCategoryDetail.values()[position].categoryNameId)
        }.attach()

    }

    private fun notifyBasketCount(foodMenuListInBasket: List<RestaurantFoodEntity>?) = with(binding){
        basketCountTextView.text = if(foodMenuListInBasket.isNullOrEmpty()){
            "0"
        }else{
            getString(R.string.basket_count, foodMenuListInBasket.size)
        }
        basketButton.setOnClickListener {
            if(firebaseAuth.currentUser == null){
                alertLoginNeed{
                    lifecycleScope.launch{
                        menuChangeEventBus.changeMenu(MainTabMenu.MY)
                        finish()
                    }


                }

            }else{
                startActivity(
                    OrderMenuListActivity.newIntent(this@RestaurantDetailActivity)
                )


            }

        }

    }
    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("로그인이 필요합니다.")
            .setMessage("주문하려면 로그인이 필요합니다. My탭으로 이동하시겠습니까?")
            .setPositiveButton("이동") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    private fun alertClearNeedInBasket(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("장바구니에는 같은 가게의 메뉴만 담을 수 있습니다.")
            .setMessage("선택하신 메뉴를 장바구니에 담을 경우 이전에 담은 메뉴가 삭제됩니다.")
            .setPositiveButton("담기") { dialog, _ ->
                viewModel.notifyClearBasket()
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }



}