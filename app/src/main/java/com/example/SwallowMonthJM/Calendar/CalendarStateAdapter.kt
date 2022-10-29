package com.example.SwallowMonthJM.Calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//calendarMonthFragment 의 어댑터
class CalendarStateAdapter(fragmentActivity: FragmentActivity)
    :FragmentStateAdapter(fragmentActivity){

    //현재 달에 대한 정보
    @RequiresApi(Build.VERSION_CODES.O)
    val dateMonth: Int = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM")).toInt()

    private var pageCount = Int.MAX_VALUE
    //무한 반복을 위함 infinite view
    val fragmentPosition = pageCount/2

    override fun getItemCount(): Int = pageCount

    @RequiresApi(Build.VERSION_CODES.O)
    override fun createFragment(position: Int): Fragment {
        val calendarMonthFragment = CalendarMonthFragment(dateMonth)
        calendarMonthFragment.pageIndex = position
        return calendarMonthFragment
    }
}