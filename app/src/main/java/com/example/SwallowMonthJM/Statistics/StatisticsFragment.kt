package com.example.SwallowMonthJM.Statistics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.StatisticsViewBinding
import java.text.SimpleDateFormat
import java.util.*

class StatisticsFragment() : Fragment() {
    var pageIndex = 0

    private var _binding : StatisticsViewBinding?=null
    private val binding  get() = _binding!!
    lateinit var mainActivity: MainActivity
    lateinit var currentDate:  Date
    lateinit var fm : FragmentManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(){
        pageIndex -=(Int.MAX_VALUE / 2)
        val date = Calendar.getInstance().run {
            add(Calendar.MONTH,pageIndex)
            time
        }
        currentDate = date

        val dateTime:String = SimpleDateFormat(
            "yyyy년 MM월", Locale.KOREA
        ).format(date.time)

        binding.stDate.text = dateTime
        binding.calendarBox.fragCalenderRecycler.apply {
            val calendarAdapter = CalendarAdapterStatistics(
                mainActivity, binding.calendarBox.fragCalenderLinear,
                currentDate,mainActivity.viewModel.todayMonth
            )

            adapter = calendarAdapter
        }

    }

}