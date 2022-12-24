package com.example.SwallowMonthJM.Unit

import android.annotation.SuppressLint
import android.widget.SeekBar
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Task
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
    private var per = slideLayout.taskPer
    private var perText = slideLayout.taskPerText
    private var icon = slideLayout.taskIcon
    private var completeButton = slideLayout.completeButton
    private var seekVar = slideLayout.taskSeekBar
    private var delButton = slideLayout.taskGarbage
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