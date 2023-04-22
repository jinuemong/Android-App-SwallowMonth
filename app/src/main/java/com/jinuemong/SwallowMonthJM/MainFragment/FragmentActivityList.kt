package com.jinuemong.SwallowMonthJM.MainFragment

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
import com.jinuemong.SwallowMonthJM.Adapter.CalendarListAdapter
import com.jinuemong.SwallowMonthJM.Adapter.FragmentAdapter
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Model.DayData
import com.jinuemong.SwallowMonthJM.Model.Task
import com.jinuemong.SwallowMonthJM.TaskFragment.DoneFragment
import com.jinuemong.SwallowMonthJM.TaskFragment.TaskFragment
import com.jinuemong.SwallowMonthJM.Unit.MonthPickerDialog
import com.jinuemong.SwallowMonthJM.databinding.FragmentTaskListBinding
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

        //page 선택 시 애니메이션
        mainActivity.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position==0){
                    initAni()

                }
            }

        })

        //현재 데이터 관찰
        mainActivity.taskViewModel.taskLiveData.observe(mainActivity, Observer {
            binding.totalTask.text = getActivityNum()
            initTopData()
        })
        mainActivity.routineViewModel.routineLivData.observe(mainActivity, Observer {
            binding.totalTask.text = getActivityNum()
            initTopData()
        })

        //달 관찰
        mainActivity.viewModel.dayLiveData.observe(mainActivity, Observer {
            binding.taskListCalendar.text = mainActivity.viewModel.currentDate.keyDate
            binding.totalTask.text = getActivityNum()
            initTopData()
        })

        //일 관찰
        mainActivity.viewModel.currentDayPosition.observe(mainActivity, Observer {
            binding.totalTask.text = getActivityNum()

        })

    }

    override fun onResume() {
        super.onResume()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mainActivity.taskViewModel.apply {
            currentDoneList.value = arrayListOf()
            currentTaskList.value = arrayListOf()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun initView(){
        //상단 데이터 적용
        binding.taskListCalendar.text = mainActivity.viewModel.currentDate.keyDate
        initTopData()

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

    @SuppressLint("SetTextI18n")
    private fun initTopData(){
        binding.taskListPer.progress = mainActivity.viewModel.monthData.totalPer
        binding.taskListPerText.text = "${mainActivity.viewModel.monthData.totalPer}%"
    }

    private fun setUpListener(){
        //년도 + 달 선택 : Month Picker
        binding.taskListCalendar.setOnClickListener {
            val dig = MonthPickerDialog(mainActivity.viewModel.currentYear.value!!,mainActivity.viewModel.currentMonth.value!!)
            dig.show(fm,null)
            dig.setOnClickedListener(object : MonthPickerDialog.ButtonClickListener{
                override fun onClicked(year: Int, month: Int) {
                    changeCalendar(year,month)
                    mainActivity.viewModel.setCurrentDayPosition(0)
                    calendarListAdapter.selectedPosition = 0
                }
            })
        }
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
                        binding.bottomInTaskList.animation = mainActivity.aniList[1]
                        binding.bottomInTaskList.animation.start()
                        mainActivity.viewModel.setCurrentDayPosition(position)
                        binding.totalTask.text = getActivityNum()
                    }
                })
            }
            mainActivity.viewModel.currentDayPosition.value!!.apply {
                if (this in 0 until calendarListAdapter.itemCount) {
                    smoothScrollToPosition(this)
                }else{
                    smoothScrollToPosition(0)
                }
            }
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
                @SuppressLint("NotifyDataSetChanged")
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
            offscreenPageLimit = 2
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

        val totalTaskList = ArrayList<Task>()
        val doneTaskList = ArrayList<Task>()

        for (task in mainActivity.taskViewModel.taskLiveData.value!!){
            if (dayIndex==task.dayIndex) {
                if (task.isDone) {
                    doneTaskList.add(task)
                } else {
                    totalTaskList.add(task)
                }
            }
        }
        mainActivity.taskViewModel.apply {
            currentDoneList.value = doneTaskList
            currentTaskList.value = totalTaskList
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
        return "${totalTaskList.size+doneTaskList.size+totalRoutine-doneTaskList.size-clearRoutine} " +
                "/ ${totalTaskList.size+doneTaskList.size+totalRoutine}"
    }
}


