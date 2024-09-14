package aop.fastcampus.part06.chapter01.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

abstract class BaseFragment<VM: BaseViewModel, VB: ViewBinding>: Fragment() {

    abstract val viewModel:VM

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    //앱이 종료될 때 fetchData 가 종료되게 할것임
    private lateinit var fetchJob: Job


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initState()
    }


    open fun initState(){
        arguments?.let{
            viewModel.storeState(it)
        }
        initViews()
        fetchJob = viewModel.fetchData()
        observeData()
    }

    open fun initViews() = Unit

    abstract fun observeData()

    override fun onDestroy() {
        if(fetchJob.isActive) // Job이 들어 있을 경우 파기함
        {
            fetchJob.cancel()
        }
        super.onDestroy()

    }

}