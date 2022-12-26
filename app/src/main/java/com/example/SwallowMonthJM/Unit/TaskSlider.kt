package com.example.SwallowMonthJM.Unit

import android.annotation.SuppressLint
import android.view.View
import android.widget.SeekBar
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.databinding.SlideLayoutTaskViewBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout
class TaskSlider(
    slideLayout: View,
    private val slideFrame : SlidingUpPanelLayout,
    private val mainActivity: MainActivity,
    private val task: Task,
) {
    private var slide : SlideLayoutTaskViewBinding
    init {
        slide = SlideLayoutTaskViewBinding.bind(slideLayout)

    }
    private var name = slide.taskName
    private var level = slide.levelText
    private var per = slide.taskPer
    private var perText = slide.taskPerText
    private var icon = slide.taskIcon
    private var completeButton = slide.completeButton
    private var seekVar = slide.taskSeekBar
    private var delButton = slide.taskGarbage
    @SuppressLint("SetTextI18n")
    fun initSlide(){
        name.text = task.text
        level.text = levelText[task.level]
        icon.setImageResource(calendarIcon[task.iconType])
        perText.text = "${task.per}%"
        per.progress = task.per
        seekVar.progress = task.per

        setUpListener()
        seekbarListener()

    }

    private fun setUpListener(){

        completeButton.setOnClickListener {
            if (!task.isDone){
                slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                mainActivity.taskViewModel.doneTaskData(task)
            }
        }

        delButton.setOnClickListener {
            slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            mainActivity.taskViewModel.delTaskData(task)
        }

        icon.setOnClickListener {
            val dig = SelectIconDialog(mainActivity)
            dig.showDig()
            dig.setOnClickedListener(object :SelectIconDialog.ButtonClickListener{
                override fun onClicked(index: Int?) {
                    if (index != null) {
                        mainActivity.taskViewModel.setTaskICon(index,task)
                        icon.setImageResource(calendarIcon[index])
                    }
                }
            })
        }
    }

    private fun seekbarListener(){
        seekVar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                perText.text = "${progress}%"
                per.progress = progress
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if(!task.isDone) {
                    mainActivity.taskViewModel.setPerTask(task, seekVar.progress)
                    if (seekVar.progress == 100) {
                        slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                        mainActivity.taskViewModel.doneTaskData(task)
                    }
                }
            }

        })
    }


}