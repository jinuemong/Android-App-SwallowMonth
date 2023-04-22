package com.jinuemong.SwallowMonthJM.Calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.databinding.FragmentCalendarMonthBinding
import java.text.SimpleDateFormat
import java.util.*

//각 달을 표시하는 fragment (월 모음)
//한 달력 표시

class CalendarMonthFragment() : Fragment() {
    var pageIndex = 0
    // 상하 슬라이드 동작 제어
    private var _binding: FragmentCalendarMonthBinding? = null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    lateinit var currentDate: Date
    lateinit var fm : FragmentManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        fm = mainActivity.frManger
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarMonthBinding.inflate(inflater, container, false)
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

    private fun initView() {

        pageIndex -= (Int.MAX_VALUE / 2)
        val date = Calendar.getInstance().run {
            add(Calendar.MONTH, pageIndex)
            time
        }
        currentDate = date

        //상단 데이터 적용
        val dateTime: String = SimpleDateFormat(
            "yyyy.MM", Locale.KOREA
        ).format(date.time)

        binding.fragCalenderYYYYXX.text = dateTime

        binding.calendarBox.fragCalenderRecycler.apply {
            if (mainActivity.addViewModel.addType=="task") {
                val calendarAdapter = CalendarAdapterAddTask(
                    mainActivity, binding.calendarBox.fragCalenderLinear,
                    currentDate, mainActivity.viewModel.currentMonth.value!!,
                ).apply {
                    setOnItemClickListener(object : CalendarAdapterAddTask.OnItemClickListener {
                        override fun onItemClick() {}
                    })
                }
                adapter = calendarAdapter
            }else{
                val calendarAdapter = CalendarAdapterAddRoutine(
                    mainActivity, binding.calendarBox.fragCalenderLinear, fm,
                    currentDate, mainActivity.viewModel.currentMonth.value!!,
                ).apply {
                    setOnItemClickListener(object : CalendarAdapterAddRoutine.OnItemClickListener {
                        override fun onItemClick() {}
                    })
                }
                adapter = calendarAdapter
            }

        }
    }
}