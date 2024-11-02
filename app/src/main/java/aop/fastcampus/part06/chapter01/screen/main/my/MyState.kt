package aop.fastcampus.part06.chapter01.screen.main.my

import android.net.Uri
import androidx.annotation.StringRes
import aop.fastcampus.part06.chapter01.model.restaurant.order.OrderModel

sealed class MyState {

    object Uninitialized: MyState()

    object Loading: MyState()

    data class Login(
        val idToken: String
    ): MyState()

    sealed class Success: MyState() {

        data class Registered( // 유저가 로그인된 상태
            val userName: String,
            val profileImageUri: Uri?,
            val orderList: List<OrderModel>,
        ): Success()

        object NotRegistered: Success() // 토큰이 없는 경우

    }

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): MyState()

}