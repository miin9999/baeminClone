package aop.fastcampus.part06.chapter01.screen.main

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import aop.fastcampus.part06.chapter01.Part6Chapter01Application
import aop.fastcampus.part06.chapter01.R
import aop.fastcampus.part06.chapter01.databinding.ActivityMainBinding
import aop.fastcampus.part06.chapter01.screen.main.home.HomeFragment
import aop.fastcampus.part06.chapter01.screen.main.home.MyFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

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
            R.id.menu_my ->{
                showFragment(MyFragment.newInstance(),MyFragment.TAG)

                true
            }
            else-> false
        }
    }


    private fun showFragment(fragment: Fragment, tag: String){

        val findFragment = supportFragmentManager.findFragmentByTag(tag)

        supportFragmentManager.fragments.forEach {fm->
            supportFragmentManager.beginTransaction().hide(fm).commit()}


        findFragment?.let{
            supportFragmentManager.beginTransaction().show(it).commit()}
            ?:kotlin.run{
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer,fragment, tag)
                    .commitAllowingStateLoss()
        }

        }

}