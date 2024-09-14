package aop.fastcampus.part06.chapter01.screen.main.home

import aop.fastcampus.part06.chapter01.databinding.FragmentHomeBinding
import aop.fastcampus.part06.chapter01.databinding.FragmentMyBinding
import aop.fastcampus.part06.chapter01.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyFragment : BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding =
        FragmentMyBinding.inflate(layoutInflater)

    override fun observeData() {

    }
    companion object{
        fun newInstance() = MyFragment()

        const val TAG = "MyFragment"
    }

}