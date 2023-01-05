package com.example.SwallowMonthJM.Statistics

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class StatisticsStateAdapter(fragmentActivity:FragmentActivity)
    :FragmentStateAdapter(fragmentActivity){
    private val pageCount = Int.MAX_VALUE
    val fragmentPosition = pageCount/2

    override fun getItemCount()=pageCount

    override fun createFragment(position: Int): Fragment {

    }

}