package aop.fastcampus.part06.chapter01.screen.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

abstract class BaseActivity<VM: BaseViewModel, VB: ViewBinding>: AppCompatActivity() {

    abstract val viewModel:VM

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    //앱이 종료될 때 fetchData가 종료되게 할것임
    private lateinit var fetchJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initState()

    }

    open fun initState(){
        initViews()
        fetchJob = viewModel.fetchData()
        observeData()
    }

    open fun initViews() = Unit

    abstract fun observeData()

    override fun onDestroy() {
        if(fetchJob.isActive) // Job이 실행 중일 경우 파기함
        {
            fetchJob.cancel()
        }
        super.onDestroy()

    }

}