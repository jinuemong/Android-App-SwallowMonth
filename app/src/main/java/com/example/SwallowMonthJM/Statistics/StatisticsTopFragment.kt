package com.example.SwallowMonthJM.Statistics

import androidx.fragment.app.Fragment
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.StatisticsTopViewBinding

class StatisticsTopFragment() : Fragment() {
    var pageIndex = 0

    private var _binding : StatisticsTopViewBinding?=null
    private val binding  get() = _binding!!
    lateinit var mainActivity: MainActivity
}