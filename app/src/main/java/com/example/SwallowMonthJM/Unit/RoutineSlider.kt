package com.example.SwallowMonthJM.Unit

import android.annotation.SuppressLint
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.databinding.SlideLayoutRoutineViewBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class RoutineSlider(
    slideLayout: SlideLayoutRoutineViewBinding,
    private val slideFrame : SlidingUpPanelLayout,
    private val mainActivity: MainActivity,
    private val routine: Routine,
) {
    private var name = slideLayout.routineName
    private var icon = slideLayout.routineIcon
    private var mainText = slideLayout.routineMainText
    private var topText = slideLayout.topTextRoutine
    private var calendar = slideLayout.calendarLayout.fragCalenderRecycler
    private var completeButton = slideLayout.completeButton

    @SuppressLint("SetTextI18n")
    fun initSlide(){
        name.text = routine.text
        icon.setImageResource(calendarIcon[routine.iconType])
        mainText.text = "$routine.clearRoutine / $routine.totalRoutine"
        topText.text = routine.topText
        initCalendar()
        setUpListener()
    }

    fun setUpListener(){
        completeButton.setOnClickListener {
            slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }

    fun initCalendar(){

    }
}