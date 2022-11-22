package com.example.SwallowMonthJM.MainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.SwallowMonthJM.Calendar.CalendarStateAdapter
import com.example.SwallowMonthJM.databinding.FragmentCalendarBinding

class FragmentCalendar : Fragment() {
    private var _binding:FragmentCalendarBinding?  = null
    private val binding get() = _binding!!

    private lateinit var calendarStateAdapter : CalendarStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.inflate(inflater,container,false)
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
            //smooth = false (바로 이동)
            binding.calendarViewPager.setCurrentItem(this.fragmentPosition,false)
        }
    }
}