package aop.fastcampus.part06.chapter01.screen.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import aop.fastcampus.part06.chapter01.R
import aop.fastcampus.part06.chapter01.databinding.ActivityMainBinding
import aop.fastcampus.part06.chapter01.screen.main.home.HomeFragment
import aop.fastcampus.part06.chapter01.screen.main.like.RestaurantLikeListFragment
import aop.fastcampus.part06.chapter01.screen.main.my.MyFragment
import aop.fastcampus.part06.chapter01.util.event.MenuChangeEventBus
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private val menuChangeEventBus by inject<MenuChangeEventBus>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        observeData()

        initViews()

    }

    private fun observeData(){
        lifecycleScope.launch {
            menuChangeEventBus.mainTabMenuFlow.collect{
                goToTab(it)
            }

        }
    }



    private fun initViews() = with(binding){
        bottomNav.setOnNavigationItemSelectedListener (this@MainActivity)
        showFragment(HomeFragment.newInstance(),HomeFragment.TAG)
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_home ->{
                showFragment(HomeFragment.newInstance(),HomeFragment.TAG)
                true
            }
            R.id.menu_like ->{
                showFragment(RestaurantLikeListFragment.newInstance(),RestaurantLikeListFragment.TAG)
                true

            }
            R.id.menu_my ->{
                showFragment(MyFragment.newInstance(), MyFragment.TAG)

                true
            }
            else-> false
        }
    }

    fun goToTab(mainTabMenu: MainTabMenu){
        binding.bottomNav.selectedItemId = mainTabMenu.menuId
    }


    private fun showFragment(fragment: Fragment, tag: String){

        val findFragment = supportFragmentManager.findFragmentByTag(tag)

        supportFragmentManager.fragments.forEach {fm->
            supportFragmentManager.beginTransaction().hide(fm).commitAllowingStateLoss()}


        findFragment?.let{
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()}
            ?:kotlin.run{
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer,fragment, tag)
                    .commitAllowingStateLoss()
        }

        }

}

enum class MainTabMenu(@IdRes val menuId: Int){
    HOME(R.id.menu_home),LIKE(R.id.menu_like), MY(R.id.menu_my)
}