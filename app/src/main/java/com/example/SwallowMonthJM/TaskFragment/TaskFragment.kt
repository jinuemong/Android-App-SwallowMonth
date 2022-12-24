package com.example.SwallowMonthJM.TaskFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.SwallowMonthJM.Adapter.TaskListAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.Unit.TaskSlider
import com.example.SwallowMonthJM.databinding.FragmentTaskBinding
import com.example.SwallowMonthJM.databinding.SlideLayoutTaskViewBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout


class TaskFragment(
    private val slideFrame : SlidingUpPanelLayout,
    private val slideLayout: SlideLayoutTaskViewBinding
) : Fragment() {
    private var _binding: FragmentTaskBinding?=null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    private lateinit var taskListAdapter:TaskListAdapter

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
        _binding = FragmentTaskBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(){
        var day = mainActivity.viewModel.currentMonthArr[mainActivity.viewModel.currentDayPosition.value!!]
        initAdapter(day)
        binding.taskView.adapter  =taskListAdapter

        mainActivity.viewModel.dayLiveData.observe(mainActivity, Observer {
            day.taskList?.let {
                (binding.taskView.adapter as TaskListAdapter).setData(it)
            }
        })

        mainActivity.viewModel.currentDayPosition.observe(mainActivity, Observer { dayIndex->
            day = mainActivity.viewModel.currentMonthArr[dayIndex]
            initAdapter(day)
            binding.taskView.adapter  =taskListAdapter
        })

    }

    private fun initAdapter(day : DayData){
        taskListAdapter = TaskListAdapter(mainActivity,day.taskList,false)
        taskListAdapter.apply {
            setOnItemClickListener(object :TaskListAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    val task = day.taskList!![position]
                    val slideLayout = slideLayout
                    val taskSlide = TaskSlider(slideLayout,slideFrame,mainActivity,task)
                    taskSlide.initSlide()
                    slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                }
            })
        }
    }
}