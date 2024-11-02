package aop.fastcampus.part06.chapter01.screen.main.my

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aop.fastcampus.part06.chapter01.R
import aop.fastcampus.part06.chapter01.data.entity.OrderEntity
import aop.fastcampus.part06.chapter01.data.preference.AppPreferenceManager
import aop.fastcampus.part06.chapter01.data.repository.order.DefaultOrderRepository
import aop.fastcampus.part06.chapter01.data.repository.order.OrderRepository
import aop.fastcampus.part06.chapter01.data.repository.user.UserRepository
import aop.fastcampus.part06.chapter01.model.restaurant.order.OrderModel
import aop.fastcampus.part06.chapter01.screen.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val appPreferenceManager: AppPreferenceManager,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) :BaseViewModel(){

    val myStateLiveData = MutableLiveData<MyState>(MyState.Uninitialized)
    //private val firebaseAuth by lazy{ FirebaseAuth.getInstance() }


    override fun fetchData(): Job = viewModelScope.launch {
        myStateLiveData.value = MyState.Loading
        //Log.d("fefevvv",appPreferenceManager.getIdToken().toString())
        appPreferenceManager.getIdToken()?.let{
            myStateLiveData.value = MyState.Login(it) // 로그인 성공 시
        }?: kotlin.run{
            myStateLiveData.value = MyState.Success.NotRegistered

        }
    }


    fun saveToken(idToken: String) = viewModelScope.launch {

        withContext(Dispatchers.IO){
            appPreferenceManager.putIdToken(idToken)
            fetchData()

        }

    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let{ user->
            when(val orderMenusResult = orderRepository.getAllOrderMenus(user.uid)){
                is DefaultOrderRepository.Result.Success<*> ->{
                    val orderList = orderMenusResult.data as List<OrderEntity>
                    myStateLiveData.value = MyState.Success.Registered(
                        userName = user.displayName ?: "익명",
                        profileImageUri = user.photoUrl,
                        orderList = orderList.map{
                            OrderModel(
                                id = it.hashCode().toLong(),
                                orderId = it.id,
                                userId = it.userId,
                                restaurantId = it.restaurantId,
                                foodMenuList = it.foodMenuList,
                                restaurantTitle = it.restaurantTitle

                            )
                        }
                    )
                }

                is DefaultOrderRepository.Result.Error -> {
                    myStateLiveData.value = MyState.Error(
                        R.string.request_error,
                        orderMenusResult.e
                    )
                }

                else -> Unit
            }

        } ?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }

    }

    fun signOut() = viewModelScope.launch {

        withContext(Dispatchers.IO){
            appPreferenceManager.removeIdToken()
        }
        userRepository.deleteALlUserLikedRestaurant()
        fetchData()

    }
}