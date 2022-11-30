package com.example.SwallowMonthJM.MainFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.DayData
import com.example.SwallowMonthJM.Unit.dayOfWeek
import com.example.SwallowMonthJM.databinding.FragmentTaskListBinding
import com.example.SwallowMonthJM.databinding.ItemCalendarHorizontalBinding

// home

class FragmentTaskList : Fragment() {
    private var _binding : FragmentTaskListBinding?=null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpListener()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        binding.taskListCalendar.text = mainActivity.viewModel.dateTime
        binding.taskListPer.progress = mainActivity.viewModel.totalPer
        binding.taskListPerText.text = mainActivity.viewModel.totalPer.toString()+"%"
        initRecyclerView()
    }

    private fun setUpListener(){

    }

    private fun initRecyclerView(){
        val todayIndex = mainActivity.viewModel.currentDate.todayIndex

        binding.taskListHoCalendar.apply {
            setHasFixedSize(true)
            adapter = CalendarListAdapter(mainActivity,mainActivity.viewModel.currentDate.dateList,todayIndex).apply {
                setOnItemClickListener(object : CalendarListAdapter.OnItemClickListener {
                    override fun onItemClick(item: DayData, position: Int) {
                        Log.d("item",position.toString()+":"+item.day)
                    }

                })
            }

            smoothScrollToPosition(todayIndex)
        }
    }
}

class CalendarListAdapter(
    private val mainActivity: MainActivity,
    private val dataSet : ArrayList<DayData>,
    private val todayIndex:Int
):RecyclerView.Adapter<CalendarListAdapter.CalendarListItemHolder>(){
    private var selectedPosition = todayIndex

    private lateinit var binding: ItemCalendarHorizontalBinding
    private var onItemClickListener: OnItemClickListener?=null


    interface OnItemClickListener{
        fun onItemClick(item : DayData,position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    inner class CalendarListItemHolder(val binding: ItemCalendarHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DayData) {
//            binding.itemHoDayNum.text = dataSet[absoluteAdapterPosition].day.toString()
//            binding.itemHoDayText.setText(dayOfWeek[absoluteAdapterPosition%7])

            if(selectedPosition==absoluteAdapterPosition){
                binding.setChecked()
            }else{
                binding.setUnchecked()
            }

            if(onItemClickListener!=null){
                binding.root.setOnClickListener {
                    onItemClickListener?.onItemClick(item, position = absoluteAdapterPosition)
                    if (selectedPosition!=absoluteAdapterPosition){
                        binding.setChecked()
                        notifyItemChanged(selectedPosition)
                        selectedPosition = absoluteAdapterPosition
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarListItemHolder {
        binding = ItemCalendarHorizontalBinding.inflate(LayoutInflater.from(mainActivity),parent,false)
        return CalendarListItemHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CalendarListItemHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.binding.itemHoDayNum.text = dataSet[position].day.toString()
        holder.binding.itemHoDayText.setText(dayOfWeek[(position%7)])

//        holder.binding.root.setOnClickListener {
//            val k = mainActivity.viewModel
//                .getKeyData(dataSet[holder.absoluteAdapterPosition].monthIndex,
//                    dataSet[holder.absoluteAdapterPosition].day.toString())
//            Log.d("k",k)
//        }
    }

    override fun getItemCount(): Int =dataSet.size

    @SuppressLint("ResourceAsColor")
    private fun ItemCalendarHorizontalBinding.setChecked() =
        itemBack.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.color_type1))

    @SuppressLint("ResourceAsColor")
    private fun ItemCalendarHorizontalBinding.setUnchecked() =
        itemBack.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.color_type3))
}