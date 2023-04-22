package com.jinuemong.SwallowMonthJM.AddTaskRoutineFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jinuemong.SwallowMonthJM.Calendar.CalendarStateAdapter
import com.jinuemong.SwallowMonthJM.databinding.FragmentTaskOneBinding

class SelectCalendarFragment : Fragment() {
    private var _binding:FragmentTaskOneBinding?=null
    private val binding get() = _binding!!
    private lateinit var calendarStateAdapter : CalendarStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskOneBinding.inflate(inflater,container,false)
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
        calendarStateAdapter = CalendarStateAdapter(requireActivity())
        binding.calendarViewPager.adapter = calendarStateAdapter
        calendarStateAdapter.apply {
            binding.calendarViewPager.setCurrentItem(this.fragmentPosition,false)
        }
    }

}