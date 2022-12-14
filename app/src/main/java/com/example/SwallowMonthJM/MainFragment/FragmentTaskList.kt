package com.example.SwallowMonthJM.MainFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.SwallowMonthJM.Adapter.CalendarListAdapter
import com.example.SwallowMonthJM.Adapter.FragmentAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayData
import com.example.SwallowMonthJM.TaskFragment.DoneFragment
import com.example.SwallowMonthJM.TaskFragment.TaskFragment
import com.example.SwallowMonthJM.Unit.MonthPickerDialog
import com.example.SwallowMonthJM.databinding.FragmentTaskListBinding
import com.google.android.material.tabs.TabLayoutMediator

// home

class FragmentTaskList : Fragment() {
    private var _binding : FragmentTaskListBinding?=null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    private lateinit var fm : FragmentManager
    private lateinit var fragmentPageAdapter: FragmentAdapter

    private var tabText = arrayOf(
        "Todo","Done"
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        fm = (activity as MainActivity).supportFragmentManager

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
        binding.taskListCalendar.text = mainActivity.viewModel.currentDate.keyDate
        binding.taskListPer.progress = mainActivity.viewModel.totalPer
        binding.taskListPerText.text = mainActivity.viewModel.totalPer.toString()+"%"

        //?????? task + routine ??????

        mainActivity.routineViewModel.setTotalRoutine()
        binding.totalTask.text = mainActivity.viewModel.getTotalTask()

        mainActivity.viewModel.dayLiveData.observe(mainActivity, Observer {
            binding.totalTask.text = mainActivity.viewModel.getTotalTask()
        })

        mainActivity.routineViewModel.routineLivData.observe(mainActivity, Observer {
            mainActivity.routineViewModel.setTotalRoutine()
            binding.totalTask.text = mainActivity.viewModel.getTotalTask()
        })

        initRecyclerView()
        initPager()
        initAni()
    }
    private fun initAni(){
        binding.topInTaskList.animation = mainActivity.aniList[2]
        binding.bottomInTaskList.animation = mainActivity.aniList[1]
        binding.taskListHoCalendar.animation = mainActivity.aniList[0]
        binding.topInTaskList.animation.start()
        binding.bottomInTaskList.animation.start()
        binding.taskListHoCalendar.animation.start()
    }

    private fun setUpListener(){
        binding.taskListCalendar.setOnClickListener {
            val dig = MonthPickerDialog(mainActivity.viewModel.currentYear.value!!,mainActivity.viewModel.currentMonth.value!!)
            dig.show(fm,null)
            dig.setOnClickedListener(object : MonthPickerDialog.ButtonClickListener{
                override fun onClicked(year: Int, month: Int) {
                    changeCalendar(year,month)
                }
            })
        }
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
                        mainActivity.routineViewModel.setTotalRoutine()
                        binding.totalTask.text = mainActivity.viewModel.getTotalTask()
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
            addFragment(TaskFragment(binding.slideFrame,binding.slideLayout))
            addFragment(DoneFragment(binding.slideFrame,binding.slideLayout))
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

    @SuppressLint("SetTextI18n")
    private fun changeCalendar(year:Int, month:Int){
        mainActivity.viewModel.apply {
            initCurrentData(getDate(year,month))
            binding.taskListCalendar.text = this.currentDate.keyDate
        }
        initRecyclerView()
        initAni()
    }
}


