package com.example.SwallowMonthJM.Statistics

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
import com.example.SwallowMonthJM.databinding.StatisticsViewBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.text.SimpleDateFormat
import java.util.*


class OneStatisticsFragment(
    private val slideFrame : SlidingUpPanelLayout,
    private val slideLayout: View,
) : Fragment() {
    var pageIndex = 0

    private var _binding : StatisticsViewBinding?=null
    private val binding  get() = _binding!!
    lateinit var mainActivity: MainActivity
    lateinit var currentDate:  Date
    lateinit var fm : FragmentManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
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

        /////////////////////////////
        //여기부분은 캘린더의 데이터에 따라 수정해주어야 하낟 !!
        binding.statisticsRecycler.adapter = TodayTaskListAdapter(
            mainActivity,
            mainActivity.viewModel.dayLiveData.value!!,
            mainActivity.routineViewModel.routineLivData.value!!,
            slideFrame,
            slideLayout
        )
        mainActivity.viewModel.currentMonth.observe(mainActivity, Observer {
            mainActivity.viewModel.currentMonthArr.apply {
                if (this.size>0) {
                    (binding.statisticsRecycler.adapter as TodayTaskListAdapter)
                        .setData(this)
                }
            }
        })

        mainActivity.viewModel.apply {

        }
    }

}