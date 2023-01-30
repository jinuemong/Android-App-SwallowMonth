package com.example.SwallowMonthJM.Statistics

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.example.SwallowMonthJM.Adapter.TodayTaskListAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Unit.MonthPickerDialog
import com.example.SwallowMonthJM.databinding.StatisticsViewBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout


class OneStatisticsFragment(
    private val slideFrame : SlidingUpPanelLayout,
    private val slideLayout: View,
) : Fragment() {
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
        initData()
        setUpListener()

        //month data Change 관찰
        mainActivity.viewModel.eventStatistics.observe(mainActivity, Observer {
            initData()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initData(){
        //상단 데이터 설정
        binding.taskListCalendar.text = mainActivity.viewModel.currentDate.keyDate
        binding.stDate.text = mainActivity.viewModel.currentDate.topDate

        //캘린더 설정
        binding.calendarBox.fragCalenderRecycler.apply {
            val calendarAdapter = CalendarAdapterStatistics(
                mainActivity, binding.calendarBox.fragCalenderLinear,
                mainActivity.viewModel.currentDate.calendar.time
                ,mainActivity.viewModel.todayMonth
            )
            adapter = calendarAdapter
        }

        //리싸이클러 설정
        binding.statisticsRecycler.adapter = TodayTaskListAdapter(
            mainActivity,
            mainActivity.taskViewModel.taskLiveData.value!!,
            mainActivity.viewModel.dayLiveData.value!!,
            mainActivity.routineViewModel.routineLivData.value!!,
            slideFrame, slideLayout
        )

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

    @SuppressLint("SetTextI18n")
    private fun changeCalendar(year:Int, month:Int){
        mainActivity.viewModel.apply {
            setCurrentData(getDate(year,month),mainActivity)
        }
        initData()
    }
}