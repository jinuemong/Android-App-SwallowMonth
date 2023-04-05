package com.example.SwallowMonthJM.DetailView

import android.annotation.SuppressLint
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.Calendar.CalendarAdapterRoutine
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.SelectIconDialog
import com.example.SwallowMonthJM.Unit.calendarIcon
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

    @SuppressLint("SetTextI18n")
    fun initSlide(){
        name.text = routine.text
        icon.setImageResource(calendarIcon[routine.iconType])
        mainText.text = "${routine.clearRoutine} / ${routine.totalRoutine}"
        topText.text = routine.topText
        initCalendar()
        setUpListener()
    }

    private fun setUpListener(){
        completeButton.setOnClickListener {
            val todayPosition =mainActivity.viewModel.currentDate.prevTail-
                    mainActivity.viewModel.todayDayPosition

            if (dPosition==todayPosition) {

                val state = slideFrame.panelState
                if (state ==  SlidingUpPanelLayout.PanelState.COLLAPSED){
                    slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED

                }else if (state == SlidingUpPanelLayout.PanelState.EXPANDED){
                    slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED

                }

                val dayRoutine = routine.dayRoutinePost.find {
                    it.dayIndex == dPosition
                }

                if (dayRoutine != null ) {
                    mainActivity.routineViewModel.doneRoutineData(routine, dayRoutine)
                }
            }else{
                Toast.makeText(mainActivity,"Not Today!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        icon.setOnClickListener {
            val dig = SelectIconDialog(mainActivity)
            dig.showDig()
            dig.setOnClickedListener(object : SelectIconDialog.ButtonClickListener{
                override fun onClicked(index: Int?) {
                    if (index!=null){
                        mainActivity.routineViewModel.setRoutineIcon(index,routine)
                        icon.setImageResource(calendarIcon[index])
                    }
                }

            })
        }
    }

    private fun initCalendar(){
        calendar.adapter = CalendarAdapterRoutine(
            mainActivity, calendarLinear,routine
        )
    }
}