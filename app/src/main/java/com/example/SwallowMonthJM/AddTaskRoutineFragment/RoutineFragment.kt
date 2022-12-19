package com.example.SwallowMonthJM.AddTaskRoutineFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.SwallowMonthJM.Calendar.CalendarAdapterAddTask
import com.example.SwallowMonthJM.databinding.FragmentRoutineBinding

//시작일

class RoutineFragment : Fragment() {
    private var _binding:FragmentRoutineBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarAdapter: CalendarAdapterAddTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoutineBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}