package aop.fastcampus.part06.chapter01.screen.mylocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import aop.fastcampus.part06.chapter01.R
import aop.fastcampus.part06.chapter01.data.entity.LocationLatLngEntity
import aop.fastcampus.part06.chapter01.data.entity.MapSearchInfoEntity
import aop.fastcampus.part06.chapter01.databinding.ActivityMainBinding
import aop.fastcampus.part06.chapter01.databinding.ActivityMyLocationBinding
import aop.fastcampus.part06.chapter01.screen.base.BaseActivity
import aop.fastcampus.part06.chapter01.screen.main.home.HomeViewModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MyLocationActivity : BaseActivity<MyLocationViewModel,ActivityMyLocationBinding>(),OnMapReadyCallback {


    companion object{

        const val CAMERA_ZOOM_LEVEL = 17f

        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context,MyLocationActivity::class.java).apply{
                putExtra(HomeViewModel.MY_LOCATION_KEY,mapSearchInfoEntity)
            }
    }


    override val viewModel by viewModel<MyLocationViewModel>{
        parametersOf(
            intent.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
        )
    }

    override fun getViewBinding(): ActivityMyLocationBinding = ActivityMyLocationBinding.inflate(layoutInflater)


    private lateinit var map : GoogleMap

    private var isMapInitialized: Boolean = false

    private var isChangeLocation: Boolean = false


    override fun onMapReady(p0: GoogleMap) {
        this.map = p0 ?: return // null로 넘어오는 경우도 있어서 null일 경우 처리
        viewModel.fetchData()
    }




    override fun initViews() =with(binding){
        toolbar.setNavigationOnClickListener {
            finish()
        }
        confirmButton.setOnClickListener{
            viewModel.confirmSelectLocation()
        }
        setupGoogleMap()

    }

    private fun setupGoogleMap(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun observeData() = viewModel.myLocationStateLiveData.observe(this) {
        when(it){
            is MyLocationState.Loading -> {
                handleLoadingState()

            }

            is MyLocationState.Success ->{
                if(::map.isInitialized){
                    handleSuccessState(it)
                }

            }
            is MyLocationState.Confirm ->{
                setResult(Activity.RESULT_OK,Intent().apply{
                    putExtra(HomeViewModel.MY_LOCATION_KEY,it.mapSearchInfoEntity)
                })
                finish()


            }

            is MyLocationState.Error ->{
                Toast.makeText(this,it.messageId, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    private fun handleLoadingState() = with(binding){
        locationLoading.isVisible = true
        locationTitleText.text= getString(R.string.loading)

    }
    private fun handleSuccessState(state: MyLocationState.Success) = with(binding){
        val mapSearchInfo = state.mapSearchInfoEntity
        locationLoading.isGone = true
        locationTitleText.text = mapSearchInfo.fullAddress

        if(isMapInitialized.not()) { // 초기화가 안되어있을때
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mapSearchInfo.locationLatLng.latitude,
                        mapSearchInfo.locationLatLng.longitude
                        ),CAMERA_ZOOM_LEVEL
                )

            )

            // 카메라가 1초이상 멈추면 그 위치에 대한 api를 불러옴
            map.setOnCameraIdleListener { // 현재 지도가 멈춰 있는지 판단하는 함수, 리스너 달아주기
                if(isChangeLocation.not()){ // 지도가 멈추면(위치가 변경안되면) 데이터를 바꿀 수 있게 할거임
                    isChangeLocation = true // 데이터가 바뀌고 있다는 상태를 알려주고
                    Handler(Looper.getMainLooper()).postDelayed({
                        val cameraLatLng = map.cameraPosition.target
                        viewModel.changeLocationInfo(
                            LocationLatLngEntity(
                                cameraLatLng.latitude,
                                cameraLatLng.longitude
                            )
                        )
                        isChangeLocation = false
                    },1000)


                }

            }

            isMapInitialized = true
        }

    }





}