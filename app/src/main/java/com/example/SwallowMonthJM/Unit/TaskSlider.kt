package com.example.SwallowMonthJM.Unit

import android.annotation.SuppressLint
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.SlideLayoutTaskViewBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class TaskSlider(
    slideLayout: SlideLayoutTaskViewBinding,
    private val slideFrame : SlidingUpPanelLayout,
    private val mainActivity: MainActivity,
    private val task: Task,
) {
    private var name = slideLayout.taskName
    private var level = slideLayout.levelText
    private var per = slideLayout.taskBar
    private var perText = slideLayout.taskPerText
    private var icon = slideLayout.taskIcon
    private var completeButton = slideLayout.completeButton

    @SuppressLint("SetTextI18n")
    fun initSlide(){
        name.text = task.text
        level.text = levelText[task.level]
        icon.setImageResource(calendarIcon[task.iconType])
        perText.text = "${task.per}%"

        setUpListener()
    }

    private fun setUpListener(){
        completeButton.setOnClickListener {
            if (!task.isDone){
                slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                mainActivity.viewModel.doneTaskData(task)
            }
        }

        icon.setOnClickListener {
            val dig = SelectIconDialog(mainActivity)
            dig.showDig()
            dig.setOnClickedListener(object :SelectIconDialog.ButtonClickListener{
                override fun onClicked(index: Int?) {
                    if (index != null) {
                        mainActivity.viewModel.setTaskICon(task,index)
                    }
                }

            })
        }
    }


}