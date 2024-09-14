package aop.fastcampus.part06.chapter01.screen.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected var stateBundle: Bundle? = null

    open fun fetchData(): Job = viewModelScope.launch {
    }

    //view의 어떤 상태를 저장하는 곳
    //액티비티 또는 프래그먼트가 완전히 종료되기 전까진 이 데이터가 유지될 것임
    open fun storeState(stateBundle: Bundle){
        this.stateBundle = stateBundle

    }
}