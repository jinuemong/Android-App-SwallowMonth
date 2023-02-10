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

class FragmentActivityList : Fragment() {
    private var _binding : FragmentTaskListBinding?=null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    private lateinit var fm : FragmentManager
    private lateinit var fragmentPageAdapter: FragmentAdapter
    private lateinit var calendarListAdapter: CalendarListAdapter
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
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        //data가 갱신된 후에 view 그리기
//        mainActivity.viewModel.eventSetData.observe(mainActivity, Observer {
//            initView()
//        })
    }

    override fun onResume() {
        super.onResume()
    }


    @SuppressLint("SetTextI18n")
    private fun initView(){
        //상단 데이터 적용
        binding.taskListCalendar.text = mainActivity.viewModel.currentDate.keyDate
        binding.taskListPer.progress = mainActivity.viewModel.monthData.totalPer
        binding.taskListPerText.text = mainActivity.viewModel.monthData.totalPer.toString()+"%"


        initRecyclerView()
        initPager()
        binding.totalTask.text = getActivityNum()
        initAni()
        setUpListener()
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
        //년도 + 달 선택 : Month Picker
        binding.taskListCalendar.setOnClickListener {
            val dig = MonthPickerDialog(mainActivity.viewModel.currentYear.value!!,mainActivity.viewModel.currentMonth.value!!)
            dig.show(fm,null)
            dig.setOnClickedListener(object : MonthPickerDialog.ButtonClickListener{
                override fun onClicked(year: Int, month: Int) {
                    changeCalendar(year,month)
                }
            })
        }

        //데이터 변화 관찰
        mainActivity.taskViewModel.taskLiveData.observe(mainActivity, Observer {
            binding.totalTask.text = getActivityNum()
        })
        mainActivity.routineViewModel.routineLivData.observe(mainActivity, Observer {
            binding.totalTask.text = getActivityNum()
        })
    }

    //현재 리싸이클러 뷰 갱신
    private fun initRecyclerView(){
        //어댑터 생성
        calendarListAdapter = CalendarListAdapter(mainActivity,
            mainActivity.viewModel.currentDate.dateList,
            mainActivity.viewModel.currentDate)

        //어댑터 연결
        binding.taskListHoCalendar.apply {
            setHasFixedSize(true)
            adapter = calendarListAdapter.apply {
                setOnItemClickListener(object : CalendarListAdapter.OnItemClickListener {
                    override fun onItemClick(item: DayData, position: Int) {
                        mainActivity.viewModel.setCurrentDayPosition(position)
                        binding.totalTask.text = getActivityNum()
                        binding.bottomInTaskList.animation = mainActivity.aniList[1]
                        binding.bottomInTaskList.animation.start()
                    }
                })
            }
            smoothScrollToPosition(mainActivity.viewModel.currentDayPosition.value!!)
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
            setCurrentData(getDate(year,month),mainActivity)
            binding.taskListCalendar.text = this.currentDate.keyDate
        }
        initRecyclerView()
        binding.totalTask.text = getActivityNum()
        initAni()
    }

    fun getActivityNum() : String{
        //task + routine 수
        val dayIndex = mainActivity.viewModel.currentDayPosition.value
        val totalTask = mainActivity.taskViewModel.taskLiveData.value!!.count {
            it.dayIndex==dayIndex
        }
        val doneTask = mainActivity.taskViewModel.taskLiveData.value!!.count{
            it.dayIndex==dayIndex && it.isDone
        }
        var totalRoutine = 0
        var clearRoutine = 0
        for (routine in mainActivity.routineViewModel.routineLivData.value!!){

            totalRoutine += routine.dayRoutinePost.count {
                it.dayIndex == dayIndex
            }
            clearRoutine += routine.dayRoutinePost.count {
                it.dayIndex == dayIndex && it.clear
            }

        }
        return "${totalTask+totalRoutine-doneTask-clearRoutine} / ${totalTask+totalRoutine}"
    }
}


