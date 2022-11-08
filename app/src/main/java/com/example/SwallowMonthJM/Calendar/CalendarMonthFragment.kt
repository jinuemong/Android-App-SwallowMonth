package com.example.SwallowMonthJM.Calendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.FragmentCalendarMonthBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.text.SimpleDateFormat
import java.util.*

//각 달을 표시하는 fragment (월 모음)
class CalendarMonthFragment(private val dateMonth:Int) : Fragment() {
    var pageIndex = 0
    // 상하 슬라이드 동작 제어
    private lateinit var state: SlidingUpPanelLayout.PanelState
    private var _binding: FragmentCalendarMonthBinding?= null
    private val binding get()=_binding!!
    lateinit var mContext: Context
    lateinit var currentDate: Date
    lateinit var calendarAdapter: CalendarAdapter

    override fun onAttach(context: Context) {
        Log.d("생명주기 확인하기","onAttach")
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
        Log.d("생명주기 확인하기","onCreateView")
        _binding = FragmentCalendarMonthBinding.inflate(inflater,container,false)
        state = binding.slideFrame.panelState
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("생명주기 확인하기","onViewCreated")

        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    override fun onDestroyView() {
        Log.d("생명주기 확인하기","onDestroyView")

        super.onDestroyView()
        binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        _binding=null
    }
    private fun initView(){
        Log.d("생명주기 확인하기","initView")

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

            addOnItemTouchListener(object :RecyclerView.OnItemTouchListener{
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    //터치 동작을 가로챔
                    val child = rv.findChildViewUnder(e.x,e.y)
                    if (child!=null){
                        val position = rv.getChildAdapterPosition(child)
                        val view = rv.layoutManager?.findViewByPosition(position)
                        view?.setOnClickListener {
                            if(state==SlidingUpPanelLayout.PanelState.COLLAPSED){ //열기
                                binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                            }else if (state == SlidingUpPanelLayout.PanelState.EXPANDED) { //닫기
                                binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
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