package com.example.SwallowMonthJM.Calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentCalendarMonthBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.text.SimpleDateFormat
import java.util.*

//각 달을 표시하는 fragment (월 모음)
class CalendarMonthFragment(private val dateMonth: Int) : Fragment() {
    var pageIndex = 0

    // 상하 슬라이드 동작 제어
    private var _binding: FragmentCalendarMonthBinding? = null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    lateinit var currentDate: Date
    lateinit var calendarAdapter: CalendarAdapter

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
        binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
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
            "yyyy년 MM월", Locale.KOREA
        ).format(date.time)

        binding.fragCalenderYYYYXX.text = dateTime
        calendarAdapter = CalendarAdapter(
            mainActivity, binding.fragCalenderLinear, currentDate, dateTime, dateMonth
        )
        binding.fragCalenderRecycler.apply {
            adapter = calendarAdapter

            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (e.action == MotionEvent.ACTION_DOWN &&
                        binding.slideFrame.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED
                    ) {
                        val child = rv.findChildViewUnder(e.x, e.y)
                        if (child != null) {
                            val position = rv.getChildAdapterPosition(child)
                            val view = rv.layoutManager?.findViewByPosition(position)
                            val day =
                                view?.findViewById<TextView>(R.id.calendar_text)?.text.toString()
                            //icon 추가 시 icon 이미지 변경
                            val todoIcon = view?.findViewById<ImageView>(R.id.is_it_todo)
                            view?.setOnClickListener {
                                if (binding.slideFrame.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) { //열기
                                    binding.slideFrame.panelState =
                                        SlidingUpPanelLayout.PanelState.ANCHORED
                                    val slideLayout = binding.slideLayout
                                    val calendarSlider =
                                        CalendarSlider(slideLayout, mainActivity)
                                    calendarSlider.initView(dateTime, day)
                                }
                            }
                        }
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
    }
}