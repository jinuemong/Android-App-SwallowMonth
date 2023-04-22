package com.jinuemong.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.jinuemong.SwallowMonthJM.Calendar.CustomCalendar
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Model.DayData
import com.jinuemong.SwallowMonthJM.Model.Routine
import com.jinuemong.SwallowMonthJM.Model.Task
import com.jinuemong.SwallowMonthJM.R
import com.jinuemong.SwallowMonthJM.DetailView.RoutineSlider
import com.jinuemong.SwallowMonthJM.DetailView.TaskSlider
import com.jinuemong.SwallowMonthJM.Unit.dayOfWeek
import com.jinuemong.SwallowMonthJM.databinding.ItemTodayTaskBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout

// TodayMiniTaskList의 어댑터 : 각 날짜를 표시

class TodayTaskListAdapter(
    private val mainActivity: MainActivity,
    private val taskList: ArrayList<Task>,
    dataSet: ArrayList<DayData>,
    customCalendar: CustomCalendar,
    private val routineList: ArrayList<Routine>,
) : RecyclerView.Adapter<TodayTaskListAdapter.TodayTaskListHolder>() {
    private lateinit var binding: ItemTodayTaskBinding

    private var taskSlider: View = mainActivity.slideLayout.findViewById(R.id.slide_task)
    private var routineSlider: View = mainActivity.slideLayout.findViewById(R.id.slide_routine)

    private var calendarData = customCalendar
    private var startIndex = calendarData.prevTail
    private var endIndex = calendarData.currentMaxDate + calendarData.prevTail

    //데이터 자르기
    private var dayDataSet = dataSet.subList(startIndex, endIndex)

    inner class TodayTaskListHolder(val binding: ItemTodayTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: DayData) {
            if (item.isSelected) {
                //today
                binding.topTextTodayDay.setText(R.string.TODAY)
            } else {
                binding.topTextTodayDay.setText(dayOfWeek[((absoluteAdapterPosition + startIndex) % 7)])
            }
            binding.topTextTodayTask.text = " ${item.keyDate}.${item.day}"

            binding.routineViewItemTodayTask.adapter =
                TodayMIniTaskListAdapter(mainActivity, absoluteAdapterPosition, routineList, null).apply {
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
                                    RoutineSlider(this, mainActivity.slideFrame, mainActivity,
                                        dayPosition, routine
                                    ).apply { initSlide() }

                                    mainActivity.slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED

                                }
                            }
                        }
                    })
                }

            binding.taskViewItemTodayTask.adapter =
                TodayMIniTaskListAdapter(mainActivity, absoluteAdapterPosition, null, taskList).apply {
                    // observer를 통한 recycler view 초기화 (task)
                    mainActivity.taskViewModel.taskLiveData.observe(mainActivity, Observer {
                        (binding.taskViewItemTodayTask.adapter as TodayMIniTaskListAdapter?)?.setTaskData(it)
                    })

                    //클릭 이벤트로 slider 나타내기 (task)
                    setOnItemClickListener(object : TodayMIniTaskListAdapter.OnItemClickListener {
                        override fun onItemClick(dayPosition: Int, routine: Routine?, task: Task?) {
                            routineSlider.visibility = View.GONE
                            taskSlider.apply {

                                visibility = View.VISIBLE
                                if (task != null) {
                                    TaskSlider(this, mainActivity.slideFrame, mainActivity, task)
                                        .apply { initSlide() }

                                    mainActivity.slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
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