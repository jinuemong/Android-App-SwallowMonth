package com.example.SwallowMonthJM.SupportFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.SwallowMonthJM.Calendar.CalendarAdapter
import com.example.SwallowMonthJM.databinding.FragmentRoutineOneBinding

//시작일

class RoutineFragmentOne : Fragment() {
    private var _binding:FragmentRoutineOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoutineOneBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}