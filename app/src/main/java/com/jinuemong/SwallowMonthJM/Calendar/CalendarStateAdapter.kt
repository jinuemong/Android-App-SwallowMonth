package com.jinuemong.SwallowMonthJM.Calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//calendarMonthFragment 의 어댑터
//각 달력 표시

class CalendarStateAdapter(fragmentActivity: FragmentActivity)
    :FragmentStateAdapter(fragmentActivity){

    private var pageCount = Int.MAX_VALUE
    //무한 반복을 위함 infinite view
    val fragmentPosition = pageCount/2

    override fun getItemCount(): Int = pageCount

    @RequiresApi(Build.VERSION_CODES.O)
    override fun createFragment(position: Int): Fragment {
        val calendarMonthFragment = CalendarMonthFragment()
        calendarMonthFragment.pageIndex = position
        return calendarMonthFragment
    }
}