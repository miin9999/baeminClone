package aop.fastcampus.part06.chapter01.util.event

import aop.fastcampus.part06.chapter01.screen.main.MainTabMenu
import kotlinx.coroutines.flow.MutableSharedFlow

class MenuChangeEventBus {

    val mainTabMenuFlow = MutableSharedFlow<MainTabMenu>()

    // flow의 emit을 문제 없이 호출하기 위해 suspend로 작성
    suspend fun changeMenu(menu: MainTabMenu){
        mainTabMenuFlow.emit(menu)
    }
}