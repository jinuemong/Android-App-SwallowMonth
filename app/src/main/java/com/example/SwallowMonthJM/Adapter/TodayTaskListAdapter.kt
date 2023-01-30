package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.RoutineSlider
import com.example.SwallowMonthJM.Unit.TaskSlider
import com.example.SwallowMonthJM.Unit.dayOfWeek
import com.example.SwallowMonthJM.databinding.ItemTodayTaskBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class TodayTaskListAdapter(
    private val mainActivity: MainActivity,
    private val taskList: ArrayList<Task>,
    dataSet: ArrayList<DayData>,
    private val routineList: ArrayList<Routine>,
    private val slideFrame: SlidingUpPanelLayout,
    private val slideLayout: View,
) : RecyclerView.Adapter<TodayTaskListAdapter.TodayTaskListHolder>() {
    private lateinit var binding: ItemTodayTaskBinding
    private var taskSlider: View = slideLayout.findViewById(R.id.slide_task)
    private var routineSlider: View = slideLayout.findViewById(R.id.slide_routine)
    private var dayDataSet = dataSet

    inner class TodayTaskListHolder(val binding: ItemTodayTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: DayData) {
            if (item.isSelected) {
                //today
                binding.topTextTodayDay.setText(R.string.TODAY)
            } else {
                binding.topTextTodayDay.setText(dayOfWeek[(absoluteAdapterPosition % 7)])
            }
            binding.topTextTodayTask.text = " ${item.keyDate}.${item.day}"

            binding.routineViewItemTodayTask.adapter =
                TodayMIniTaskListAdapter(mainActivity, item.day, routineList, null).apply {
                    //observer를 통한 recycler view 초기화 (루틴)
                    mainActivity.routineViewModel.routineLivData.observe(mainActivity, Observer {
                        (binding.routineViewItemTodayTask.adapter as TodayMIniTaskListAdapter?)?.setRoutineData(it)
                    })

                    //클릭 이벤트로 slider 나타내기 (루틴)
                    setOnItemClickListener(object : TodayMIniTaskListAdapter.OnItemClickListener {
                        override fun onItemClick(dayPosition: Int, routine: Routine?, task: Task?) {
                            taskSlider.visibility = View.GONE
                            routineSlider.apply {
                                visibility = View.VISIBLE
                                if (routine != null) {
                                    RoutineSlider(
                                        this,
                                        slideFrame,
                                        mainActivity,
                                        dayPosition,
                                        routine
                                    )
                                        .apply { initSlide() }
                                    val state = slideFrame.panelState
                                    if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                                        slideFrame.panelState =
                                            SlidingUpPanelLayout.PanelState.ANCHORED
                                    } else if (state == SlidingUpPanelLayout.PanelState.EXPANDED) {
                                        slideFrame.panelState =
                                            SlidingUpPanelLayout.PanelState.COLLAPSED
                                    }
                                }
                            }
                        }
                    })
                }

            binding.taskViewItemTodayTask.adapter =
                TodayMIniTaskListAdapter(mainActivity, item.day, null, taskList).apply {
                    // observer를 통한 recycler view 초기화 (task)
                    mainActivity.taskViewModel.taskLiveData.observe(mainActivity, Observer {
                        if (it.size > 0 && 0 < absoluteAdapterPosition && absoluteAdapterPosition < it.size) {
                            (binding.taskViewItemTodayTask.adapter as TodayMIniTaskListAdapter?)?.setTaskData(it)
                        }
                    })

                    //클릭 이벤트로 slider 나타내기 (task)
                    setOnItemClickListener(object : TodayMIniTaskListAdapter.OnItemClickListener {
                        override fun onItemClick(dayPosition: Int, routine: Routine?, task: Task?) {
                            routineSlider.visibility = View.GONE
                            taskSlider.apply {
                                visibility = View.VISIBLE
                                if (task != null) {
                                    TaskSlider(this, slideFrame, mainActivity, task)
                                        .apply { initSlide() }
                                    val state = slideFrame.panelState
                                    if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                                        slideFrame.panelState =
                                            SlidingUpPanelLayout.PanelState.ANCHORED
                                    } else if (state == SlidingUpPanelLayout.PanelState.EXPANDED) {
                                        slideFrame.panelState =
                                            SlidingUpPanelLayout.PanelState.COLLAPSED
                                    }
                                }
                            }
                        }
                    })
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayTaskListHolder {
        binding = ItemTodayTaskBinding.inflate(LayoutInflater.from(mainActivity), parent, false)
        return TodayTaskListHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayTaskListHolder, position: Int) {
        holder.bind(dayDataSet[position])
    }

    override fun getItemCount(): Int = dayDataSet.size



}