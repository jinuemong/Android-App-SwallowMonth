package com.jinuemong.SwallowMonthJM.DetailView

import android.annotation.SuppressLint
import android.view.View
import android.widget.*
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Model.Task
import com.jinuemong.SwallowMonthJM.R
import com.jinuemong.SwallowMonthJM.Unit.SelectIconDialog
import com.jinuemong.SwallowMonthJM.Unit.calendarIcon
import com.jinuemong.SwallowMonthJM.Unit.levelText
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class TaskSlider(
    private val slide: View,
    private val slideFrame: SlidingUpPanelLayout,
    private val mainActivity: MainActivity,
    private val task: Task,
) {

    private var name = slide.findViewById<TextView>(R.id.task_name)
    private var level = slide.findViewById<TextView>(R.id.level_text)
    private var per = slide.findViewById<ProgressBar>(R.id.task_per)
    private var perText = slide.findViewById<TextView>(R.id.task_per_text)
    private var icon = slide.findViewById<ImageView>(R.id.task_icon)
    private var completeButton = slide.findViewById<Button>(R.id.complete_button)
    private var seekVar = slide.findViewById<SeekBar>(R.id.task_seek_bar)
    private var delButton = slide.findViewById<ImageView>(R.id.task_garbage)

    @SuppressLint("SetTextI18n")
    fun initSlide() {
        name.text = task.text
        level.text = levelText[task.level]
        icon.setImageResource(calendarIcon[task.iconType])
        perText.text = "${task.per}%"
        per.progress = task.per
        seekVar.progress = task.per

        setUpListener()
        seekbarListener()
    }

    private fun setUpListener() {


        completeButton.setOnClickListener {
            if (!task.isDone && mainActivity.viewModel.checkToday(task.dayIndex)) {
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
            dig.setOnClickedListener(object : SelectIconDialog.ButtonClickListener {
                override fun onClicked(index: Int?) {
                    if (index != null) {
                        mainActivity.taskViewModel.setTaskICon(index, task)
                        icon.setImageResource(calendarIcon[index])
                    }
                }
            })
        }
    }

    private fun seekbarListener() {
        seekVar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                perText.text = "${progress}%"
                per.progress = progress
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (!task.isDone && mainActivity.viewModel.checkToday(task.dayIndex)) {
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