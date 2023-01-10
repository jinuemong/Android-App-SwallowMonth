package com.example.SwallowMonthJM.MainFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Statistics.StatisticsStateAdapter
import com.example.SwallowMonthJM.databinding.FragmentStatisticsBinding

class FragmentStatistics : Fragment() {
    private var _binding:FragmentStatisticsBinding?  = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var stateAdapter:StatisticsStateAdapter

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
        // Inflate the layout for this fragment
        _binding = FragmentStatisticsBinding.inflate(inflater,container,false)
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
        stateAdapter = StatisticsStateAdapter(requireActivity()
                ,binding.slideFrame, binding.slideLayout)

        binding.viewPagerInStatistics.adapter = stateAdapter
        stateAdapter.apply {
            binding.viewPagerInStatistics.currentItem = this.fragmentPosition
        }
    }

}