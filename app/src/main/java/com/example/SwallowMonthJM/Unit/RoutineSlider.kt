package com.example.SwallowMonthJM.Unit

import android.annotation.SuppressLint
import android.view.View
import com.example.SwallowMonthJM.Calendar.CalendarAdapterNormal
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.databinding.SlideLayoutRoutineViewBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class RoutineSlider(
    slideLayout: View,
    private val slideFrame : SlidingUpPanelLayout,
    private val mainActivity: MainActivity,
    private val dPosition:Int,
    private val routine: Routine,
) {
    private var slide : SlideLayoutRoutineViewBinding
    init {
        slide = SlideLayoutRoutineViewBinding.bind(slideLayout)
    }
    private var name = slide.routineName
    private var icon = slide.routineIcon
    private var mainText = slide.routineMainText
    private var topText = slide.topTextRoutine
    private var calendar = slide.calendarLayout.fragCalenderRecycler
    private var completeButton = slide.completeButton

    @SuppressLint("SetTextI18n")
    fun initSlide(){
        name.text = routine.text
        icon.setImageResource(calendarIcon[routine.iconType])
        mainText.text = "$routine.clearRoutine / $routine.totalRoutine"
        topText.text = routine.topText
        initCalendar()
        setUpListener()
    }

    private fun setUpListener(){
        completeButton.setOnClickListener {
            slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            routine.dayRoutineList[dPosition]?.let { dayRoutine ->
                mainActivity.routineViewModel.doneRoutineData(dayRoutine)
            }
        }
    }

    private fun initCalendar(){
        calendar.adapter = CalendarAdapterNormal(mainActivity,slide.calendarLayout.fragCalenderLinear,
        mainActivity.viewModel.todayDate,mainActivity.viewModel.currentMonth.value!!,routine.dayRoutineList[dPosition])
    }
}