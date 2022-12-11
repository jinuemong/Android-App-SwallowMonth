package com.example.SwallowMonthJM.MainFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.SwallowMonthJM.Adapter.CalendarListAdapter
import com.example.SwallowMonthJM.Adapter.FragmentAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.TaskFragment.DoneFragment
import com.example.SwallowMonthJM.TaskFragment.TaskFragment
import com.example.SwallowMonthJM.Unit.DayData
import com.example.SwallowMonthJM.databinding.FragmentTaskListBinding
import com.google.android.material.tabs.TabLayoutMediator

// home

class FragmentTaskList : Fragment() {
    private var _binding : FragmentTaskListBinding?=null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    private lateinit var fragmentPageAdapter: FragmentAdapter

    private var tabText = arrayOf(
        "Todo","Done"
    )

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

    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        binding.taskListCalendar.text = mainActivity.viewModel.dateTime
        binding.taskListPer.progress = mainActivity.viewModel.totalPer
        binding.taskListPerText.text = mainActivity.viewModel.totalPer.toString()+"%"
        initRecyclerView()
        initPager()
        initAni()
    }
    private fun initAni(){
        binding.topInTaskList.animation = mainActivity.aniList[2]
        binding.bottomInTaskList.animation = mainActivity.aniList[1]
        binding.taskListHoCalendar.animation = mainActivity.aniList[0]
    }

    private fun setUpListener(){

    }

    private fun initRecyclerView(){
        val todayIndex = mainActivity.viewModel.currentDate.todayIndex
        mainActivity.viewModel.setCurrentDayPosition(todayIndex)
        binding.taskListHoCalendar.apply {
            setHasFixedSize(true)
            adapter = CalendarListAdapter(mainActivity,mainActivity.viewModel.currentDate.dateList,todayIndex).apply {
                setOnItemClickListener(object : CalendarListAdapter.OnItemClickListener {
                    override fun onItemClick(item: DayData, position: Int) {
                        mainActivity.viewModel.setCurrentDayPosition(position)
                        binding.bottomInTaskList.animation = mainActivity.aniList[1]
                        binding.bottomInTaskList.animation.start()
                    }
                })
            }

            smoothScrollToPosition(todayIndex)
        }
    }

    private fun initPager(){
        fragmentPageAdapter = FragmentAdapter(mainActivity)
        fragmentPageAdapter.apply {
            addFragment(TaskFragment())
            addFragment(DoneFragment())
        }
        binding.taskListViewPager.apply {
            adapter = fragmentPageAdapter
            registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        TabLayoutMediator(binding.tabInTaskList,binding.taskListViewPager)
        { tab,position->
            tab.text = tabText[position]
        }.attach()
    }
}


