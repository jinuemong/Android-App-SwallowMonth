package com.example.SwallowMonthJM.Unit

import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.SlideLayoutTaskViewBinding

class TaskSlider(
    slideLayout: SlideLayoutTaskViewBinding,
    private val mainActivity: MainActivity,
    private val task: Task,
) {
    private var name = slideLayout.taskName
    private var level = slideLayout.levelText
    private var per = slideLayout.taskBar
    private var perText = slideLayout.taskPerText
    private var icon = slideLayout.taskIcon
    private var completeButton = slideLayout.completeButton

    fun initSlide(){
        name.text = task.text
        level.text = levelText[task.level]
        icon.setImageResource(calendarIcon[task.iconType])
        perText.text = "0%"

    }
}