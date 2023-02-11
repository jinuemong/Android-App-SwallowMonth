package com.example.SwallowMonthJM.Unit

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.Calendar.CalendarAdapterRoutine
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.R
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class RoutineSlider(
    private val slide: View,
    private val slideFrame : SlidingUpPanelLayout,
    private val mainActivity: MainActivity,
    private val dPosition:Int,
    private val routine: Routine,
) {

    private var name = slide.findViewById<TextView>(R.id.routine_name)
    private var icon  = slide.findViewById<ImageView>(R.id.routine_icon)
    private var mainText = slide.findViewById<TextView>(R.id.routine_main_text)
    private var topText = slide.findViewById<TextView>(R.id.top_text_routine)
    private var calendar = slide.findViewById<View>(R.id.calendar_layout)
        .findViewById<RecyclerView>(R.id.frag_calender_recycler)
    private var calendarLinear = slide.findViewById<View>(R.id.calendar_layout)
        .findViewById<LinearLayout>(R.id.frag_calender_linear)
    private var completeButton = slide.findViewById<Button>(R.id.complete_button)
    //slide 너비 설정
    private val params =
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)

    @SuppressLint("SetTextI18n")
    fun initSlide(){
        name.text = routine.text
        icon.setImageResource(calendarIcon[routine.iconType])
        mainText.text = "${routine.clearRoutine} / ${routine.totalRoutine}"
        topText.text = routine.topText
        initCalendar()
        setUpListener()
        slide.layoutParams = params
    }

    private fun setUpListener(){
        completeButton.setOnClickListener {
            val state = slideFrame.panelState
            if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
            }
            else if (state == SlidingUpPanelLayout.PanelState.EXPANDED) {
                slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }

            val dayRoutine = routine.dayRoutinePost.find {
                it.dayIndex ==dPosition
            }

            if (dayRoutine != null) {
                mainActivity.routineViewModel.doneRoutineData(routine,dayRoutine)
            }
        }
    }

    private fun initCalendar(){
        calendar.adapter = CalendarAdapterRoutine(
            mainActivity, calendarLinear,
            mainActivity.viewModel.currentDate.calendar.time,
            mainActivity.viewModel.currentMonth.value!!, routine, dPosition
        )
    }
}