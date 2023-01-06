package com.example.SwallowMonthJM.Statistics

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class StatisticsStateAdapter(
    fragmentActivity:FragmentActivity,
    private val slideFrame : SlidingUpPanelLayout,
    private val slideLayout: View,
)
    :FragmentStateAdapter(fragmentActivity){
    private val pageCount = Int.MAX_VALUE
    val fragmentPosition = pageCount/2

    override fun getItemCount()=pageCount

    override fun createFragment(position: Int): Fragment {
        val statisticsTopFragment = StatisticsFragment(slideFrame,slideLayout)
        statisticsTopFragment.pageIndex = position
        return statisticsTopFragment
    }

}