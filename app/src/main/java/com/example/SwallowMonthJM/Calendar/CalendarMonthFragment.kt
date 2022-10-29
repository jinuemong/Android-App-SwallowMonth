package com.example.SwallowMonthJM.Calendar

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.FragmentCalendarMonthBinding
import java.text.SimpleDateFormat
import java.util.*

//각 달을 표시하는 fragment
class CalendarMonthFragment(private val dateMonth:Int) : Fragment() {
    var pageIndex = 0
    private var _binding: FragmentCalendarMonthBinding?= null
    private val binding get()=_binding!!
    lateinit var mContext: Context
    lateinit var currentDate: Date
    lateinit var calendarAdapter: CalendarAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarMonthBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    private fun initView(){
        pageIndex -=(Int.MAX_VALUE/2)
        val date = Calendar.getInstance().run {
            add(Calendar.MONTH,pageIndex)
            time
        }
        currentDate = date

        //상단 데이터 적용
        val dateTime:String = SimpleDateFormat(
            "yyyy년 MM월",Locale.KOREA
        ).format(date.time)

        binding.fragCalenderYYYYXX.text = dateTime
        calendarAdapter = CalendarAdapter(mContext,binding.fragCalenderLinear
            ,currentDate,dateMonth)
        binding.fragCalenderRecycler.apply {
            adapter = calendarAdapter
        }
    }
}