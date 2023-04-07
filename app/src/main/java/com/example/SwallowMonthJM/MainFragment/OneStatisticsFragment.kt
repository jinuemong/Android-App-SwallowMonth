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
import com.example.SwallowMonthJM.Adapter.TodayTaskListAdapter
import com.example.SwallowMonthJM.Calendar.CalendarAdapterStatistics
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Unit.MonthPickerDialog
import com.example.SwallowMonthJM.databinding.StatisticsViewBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout


class OneStatisticsFragment() : Fragment() {
    private var _binding : StatisticsViewBinding?=null
    private val binding  get() = _binding!!
    lateinit var mainActivity: MainActivity
    private lateinit var fm : FragmentManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        fm = (activity as MainActivity).supportFragmentManager

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StatisticsViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCalData()
        initData()
        setUpListener()

        mainActivity.apply {
            viewModel.dayLiveData.observe(mainActivity, Observer {
                initCalData()
            })
            //상단 데이터 초기화
            taskViewModel.taskLiveData.observe(mainActivity, Observer {
                initTopData()
            })
            routineViewModel.routineLivData.observe(mainActivity, Observer {
                initTopData()
            })
            //page 선택
            mainActivity.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if(position==1){
                        initAni()
                    }
                }
            })
        }
    }


    private fun initCalData(){
        //캘린더 데이터 설정
        binding.taskListCalendar.text = mainActivity.viewModel.currentDate.keyDate
        binding.stDate.text = mainActivity.viewModel.currentDate.topDate
    }

    @SuppressLint("SetTextI18n")
    private fun initTopData(){
        //top 데이터 설정
        mainActivity.viewModel.monthData.apply {
            binding.stPer.progress = totalPer
            binding.stPerText.text = "${totalPer}%"
            binding.stPoint.text = "point:${totalPoint}"
            binding.stTotal.text = "activity:${taskCount+dayRoutineCount}"
            binding.stComplete.text = "clear:${clearRoutine + doneTask}"
        }
    }
    private fun initData(){

        //캘린더 설정
        binding.calendarBox.fragCalenderRecycler.apply {
            val calendarAdapter = CalendarAdapterStatistics(
                mainActivity, binding.calendarBox.fragCalenderLinear,
            )
            adapter = calendarAdapter
        }

        //리싸이클러 설정
        binding.statisticsRecycler.adapter = TodayTaskListAdapter(
            mainActivity,
            mainActivity.taskViewModel.taskLiveData.value!!,
            mainActivity.viewModel.dayLiveData.value!!,
            mainActivity.viewModel.currentDate,
            mainActivity.routineViewModel.routineLivData.value!!,
        )

    }

    private fun setUpListener(){
        binding.taskListCalendar.setOnClickListener {
            val dig = MonthPickerDialog(mainActivity.viewModel.currentYear.value!!,mainActivity.viewModel.currentMonth.value!!)
            dig.show(fm,null)
            dig.setOnClickedListener(object : MonthPickerDialog.ButtonClickListener{
                override fun onClicked(year: Int, month: Int) {
                    changeCalendar(year,month)
                    initAni()
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun changeCalendar(year:Int, month:Int){
        mainActivity.viewModel.apply {
            setCurrentData(getDate(year,month),mainActivity)
        }
        initData()

    }

    fun initAni(){
        binding.topMenu.animation = mainActivity.aniList[2]
        binding.leftMenu.animation = mainActivity.aniList[0]
        binding.rightMenu.animation = mainActivity.aniList[4]
        binding.statisticsRecycler.animation = mainActivity.aniList[1]
        binding.topMenu.animation.start()
        binding.leftMenu.animation.start()
        binding.rightMenu.animation.start()
        binding.statisticsRecycler.animation.start()
    }

}