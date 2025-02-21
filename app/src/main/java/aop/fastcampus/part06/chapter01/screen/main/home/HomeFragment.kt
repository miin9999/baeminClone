package aop.fastcampus.part06.chapter01.screen.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import aop.fastcampus.part06.chapter01.R
import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.entity.MapSearchInfoEntity
import aop.fastcampus.part06.chapter01.databinding.FragmentHomeBinding
import aop.fastcampus.part06.chapter01.screen.base.BaseFragment
import aop.fastcampus.part06.chapter01.screen.main.MainActivity
import aop.fastcampus.part06.chapter01.screen.main.MainTabMenu
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantCategory
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantListFragment
import aop.fastcampus.part06.chapter01.screen.main.home.restaurant.RestaurantOrder
import aop.fastcampus.part06.chapter01.screen.mylocation.MyLocationActivity
import aop.fastcampus.part06.chapter01.screen.order.OrderMenuListActivity
import aop.fastcampus.part06.chapter01.widget.adapter.RestaurantListFragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<HomeViewModel,FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    private lateinit var locationManager: LocationManager

    private lateinit var myLocationListener: MyLocationListener

    private val firebaseAuth by lazy{ FirebaseAuth.getInstance() }

    // 호출한 액티비티가 종료될 때 실행됨 !!
    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
                    ?.let{ myLocationInfo->
                        viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLng)

                }
            }
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions->

            val responsePermissions = permissions.entries.filter{
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION)
                        || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if(responsePermissions.filter { it.value == true }.size == locationPermissions.size){
                setMyLocationListener()

            }else{
                with(binding.locationTitleText){
                    setText(R.string.please_setup_your_location_permission)
                    setOnClickListener{
                        getMyLocation()
                    }
                }
                Toast.makeText(requireContext(),
                    getString(R.string.can_not_assigned_permission),Toast.LENGTH_SHORT).show()
            }

        }

    override fun initViews() = with(binding) {
        locationTitleText.setOnClickListener{
            viewModel.getMapSearchInfo()?.let{ mapInfo ->

                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(
                        requireContext(),
                        mapInfo // 현재 설정되어 있는 위치를 넘김
                    )
                )
            }
        }

        orderChipGroup.setOnCheckedChangeListener{ _, checkedId ->
            when(checkedId){
                R.id.chipDefault ->{
                    chipInitialize.isGone = true
                    changeRestaurantOrder(RestaurantOrder.DEFAULT)
                }

                R.id.chipInitialize ->{
                    chipDefault.isChecked = true

                }
                R.id.chipLowDeliveryTip->{
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.LOW_DELIVERY_TIP)
                }
                R.id.chipFastDelivery->{
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.FAST_DELIVERY)

                }
                R.id.chipTopRate->{
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.TOP_RATE)

                }

            }


        }

    }


    private fun changeRestaurantOrder(order:RestaurantOrder){
        viewPagerAdapter.fragmentList.forEach {
            it.viewModel.setRestaurantOrder(order)
        }
    }

    private fun initViewPager(locationLatLng: LocationLatLngEntity) = with(binding){

        val restaurantCategories = RestaurantCategory.values()
        Log.d("restaurantCategories",restaurantCategories.toString())

        if(::viewPagerAdapter.isInitialized.not()){ //초기화되어 있지 않다면
            orderChipGroup.isVisible = true
            val restaurantListFragmentList = restaurantCategories.map{
                RestaurantListFragment.newInstance(it,locationLatLng)
                // restaurantCategories 를 이용해서 newInstance 에서 Fragment 로 만들어온거야 그걸 다시
                // restaurantCategories 로 받아온 거임 (map 을 이용)
                // 그래서 restaurantListFragmentList 에는 "전체" 라는 enum class 의 한 부분이 뷰페이저로 들어옴
                // viewPager 안에 리사이클러뷰를 품고 있는 구조, viewPager 의 카테고리는 RestaurantCategory 라는 enum class 로 관리

            }

            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList,
                locationLatLng
                )
            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit = restaurantCategories.size
            TabLayoutMediator(tabLayout, viewPager) { tab, position->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()
        }


        if(locationLatLng != viewPagerAdapter.locationLatLngEntity){
            viewPagerAdapter.locationLatLngEntity = locationLatLng
            viewPagerAdapter.fragmentList.forEach{
                it.viewModel.setLocationLatLng(locationLatLng)

            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.checkMyBasket()

    }


    override fun observeData() {

        viewModel.homeStateLiveData.observe(viewLifecycleOwner){
            when(it){
                is HomeState.Uninitialized ->{
                    Log.d("HomeState","Uninitialized")
                    getMyLocation()
                }
                is HomeState.Loading ->{
                    Log.d("HomeState","Loading")
                    binding.locationLoading.isVisible = true
                    binding.locationTitleText.text = getString(R.string.loading)
                }
                is HomeState.Success ->{
                    Log.d("HomeState","Success")
                    binding.locationLoading.isGone = true
                    binding.locationTitleText.text = it.mapSearchInfoEntity.fullAddress
                    binding.tabLayout.isVisible = true
                    binding.filterScrollView.isVisible = true
                    binding.viewPager.isVisible = true
                    initViewPager(it.mapSearchInfoEntity.locationLatLng)
                    if(it.isLocationSame.not()){
                        Toast.makeText(requireContext(),R.string.please_set_your_current_location,Toast.LENGTH_SHORT).show()
                    }
                }
                is HomeState.Error->{
                    binding.locationLoading.isGone = true
                    binding.locationTitleText.setText(R.string.location_not_found)
                    Toast.makeText(requireContext(),it.messageId,Toast.LENGTH_SHORT).show()
                    binding.locationTitleText.setOnClickListener{
                        getMyLocation()
                    }

                }

                else -> Unit

            }
        }

        viewModel.foodMenuBasketLiveData.observe(this){
            if(it.isNotEmpty()){
                binding.basketButtonContainer.isVisible = true
                binding.basketCountTextView.text = getString(R.string.basket_count,it.size)
                binding.basketButton.setOnClickListener{
                    if(firebaseAuth.currentUser == null){
                        // 로그인이 안 되어 있을 때
                        alertLoginNeed {
                            (requireActivity() as MainActivity).goToTab(MainTabMenu.MY)
                        }

                    }else{
                        startActivity(
                            OrderMenuListActivity.newIntent(requireContext())
                        )

                    }
                }

            }else{
                binding.basketButtonContainer.isGone = true
                binding.basketButton.setOnClickListener(null)
            }
        }
    }

    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(requireContext())
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

    private fun getMyLocation(){
        if(::locationManager.isInitialized.not()) {
            locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsUnalbed = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // Log.d("fefefefdsf",isGpsUnalbed.toString()) true 반환
        if(isGpsUnalbed){
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener(){
        val minTime = 1500L
        val minDistance = 100f
        if(::myLocationListener.isInitialized.not()){
            myLocationListener = MyLocationListener()
        }
        with(locationManager){
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,minDistance,myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,minDistance,myLocationListener
            )
        }

    }

    
    companion object{

        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }

    private fun removeLocationListener(){
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized){
            locationManager.removeUpdates(myLocationListener)
        }

    }
    inner class MyLocationListener: LocationListener{
        override fun onLocationChanged(location: Location) {
            //binding.locationTitleText.text = "${location.latitude}, ${location.longitude}"
            viewModel.loadReverseGeoInformation(
                LocationLatLngEntity(
                    location.latitude,
                    location.longitude,
                )
            )
            removeLocationListener()
        }
    }
}