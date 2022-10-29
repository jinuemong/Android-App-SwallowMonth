package com.example.SwallowMonthJM

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.SwallowMonthJM.Adapter.FragmentAdapter
import com.example.SwallowMonthJM.MainFragment.FragmentCalendar
import com.example.SwallowMonthJM.MainFragment.FragmentRepeatTaskList
import com.example.SwallowMonthJM.MainFragment.FragmentTaskList
import com.example.SwallowMonthJM.MainFragment.FragmentUserUI
import com.example.SwallowMonthJM.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    lateinit var frManger :FragmentManager
    lateinit var fragmentPageAdapter:FragmentAdapter
    //click tab
    private val tintColor = ColorStateList(
        arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected)),
        intArrayOf(Color.parseColor("#629CD1"),Color.parseColor("#2C608F"))
    )
    //tab Icon
    private val iconView = arrayOf(
        R.drawable.ic_iconmonstr_calendar_9,
        R.drawable.ic_iconmonstr_tiles_list_lined,
        R.drawable.ic_iconmonstr_refresh_7,
        R.drawable.ic_iconmonstr_user_male_thin
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
    }

    private fun initView(){
        frManger = this@MainActivity.supportFragmentManager
        binding.mainTopLayout.apply {
            //상단 적용
        }
        initFragmentAdapter()
        initViewPager()
        initTabLayout()
    }
    private fun initFragmentAdapter(){
        fragmentPageAdapter = FragmentAdapter(this@MainActivity)
        fragmentPageAdapter.addFragment(FragmentCalendar())
        fragmentPageAdapter.addFragment(FragmentTaskList())
        fragmentPageAdapter.addFragment(FragmentRepeatTaskList())
        fragmentPageAdapter.addFragment(FragmentUserUI())
    }
    private fun initViewPager(){
        binding.mainMidViewpager.adapter = fragmentPageAdapter
        binding.mainMidViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
        binding.mainMidViewpager.isUserInputEnabled = false
        //스크롤 금지
    }
    private fun initTabLayout(){
        binding.mainBottomTabLayout.tabIconTint = tintColor
        TabLayoutMediator(binding.mainBottomTabLayout,binding.mainMidViewpager)
        { tab, position->
            tab.setIcon(iconView[position])
        }.attach()
    }
    fun onFragmentChange(goFragment: Fragment){
        frManger.beginTransaction().replace(R.id.main_view_container,goFragment)
            .addToBackStack(null)
            .commit()
    }

    fun onFragmentGoBack(fragment: Fragment){
        frManger.beginTransaction().remove(fragment).commit()
        frManger.popBackStack()
    }

}

