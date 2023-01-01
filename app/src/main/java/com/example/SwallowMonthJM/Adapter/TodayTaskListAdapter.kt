package com.example.SwallowMonthJM.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.dayOfWeek
import com.example.SwallowMonthJM.databinding.ItemTodayTaskBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class TodayTaskListAdapter(
    private val mainActivity: MainActivity,
    private val dataSet : ArrayList<DayData>,
    private val routineList : ArrayList<Routine>,
    private val slideFrame : SlidingUpPanelLayout,
    private val slideLayout: View,
): RecyclerView.Adapter<TodayTaskListAdapter.TodayTaskListHolder>() {
    private lateinit var binding : ItemTodayTaskBinding
    private var taskSlider : View = slideLayout.findViewById(R.id.slide_task)
    private var routineSlider : View = slideLayout.findViewById(R.id.slide_routine)

    inner class TodayTaskListHolder(val binding:ItemTodayTaskBinding)
        :RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item : DayData){
            if (item.isSelected){
                //today
                binding.topTextTodayDay.setText(R.string.TODAY)
            }else{
                binding.topTextTodayDay.setText(dayOfWeek[(absoluteAdapterPosition%7)])
            }
            binding.topTextTodayTask.text = ", ${item.keyDate} ${item.day}"

            binding.routineViewItemTodayTask.adapter =
                TodayMIniTaskListAdapter(mainActivity,item.day,routineList,null).apply {
                    setOnItemClickListener(object :TodayMIniTaskListAdapter.OnItemClickListener{
                        override fun onItemClick(dayPosition: Int, routine: Routine?, task: Task?) {

                        }
                    })
                }
            binding.taskViewItemTodayTask.adapter =
                TodayMIniTaskListAdapter(mainActivity,item.day,null,item.taskList).apply {
                    setOnItemClickListener(object :TodayMIniTaskListAdapter.OnItemClickListener{
                        override fun onItemClick(dayPosition: Int, routine: Routine?, task: Task?) {

                        }
                    })
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayTaskListHolder {
        binding = ItemTodayTaskBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return TodayTaskListHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayTaskListHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int =  dataSet.size
}