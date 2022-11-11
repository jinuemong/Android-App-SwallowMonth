package com.example.SwallowMonthJM.Calendar

import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.SlideLayoutCalendarBinding

class CalendarSlider(
    slideLayout: SlideLayoutCalendarBinding,
    private val mainActivity: MainActivity
) {
    private val topTextView = slideLayout.todoTopText
    private val editTypingView = slideLayout.todoEditText
    private val addButton = slideLayout.todoAddButton
    private val goAllGoalsButton = slideLayout.todoViewAllGoals
    private val goAddRoutineButton = slideLayout.todoAddRoutine
    private val recentlyListRecycler = slideLayout.todoRecentlyList

    fun initView(dateTime: String,day: String){
        val topText = dateTime+" "+ day + "일 일정"
        topTextView.text = topText
    }

    fun setUpListener(){
        goAllGoalsButton.setOnClickListener {
            mainActivity.viewPager.currentItem= 1
        }
        goAddRoutineButton.setOnClickListener {
            mainActivity.viewPager.currentItem = 2
        }
    }
}