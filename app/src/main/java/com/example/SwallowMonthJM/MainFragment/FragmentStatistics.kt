package com.example.SwallowMonthJM.MainFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.SwallowMonthJM.Adapter.TodayTaskListAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.databinding.FragmentStatisticsBinding

class FragmentStatistics : Fragment() {
    private var _binding:FragmentStatisticsBinding?  = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

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

        mainActivity.routineViewModel.addRoutineData(Routine(null,"test routine",2,0,10,0,0,
        "teteet",HashMap()))
        mainActivity.taskViewModel.addTaskData(0,2,Task(null,"test1",false,0,1,15))
        mainActivity.taskViewModel.addTaskData(0,2,Task(null,"test2",true,0,1,15))
        binding.statisticsRecycler.adapter = TodayTaskListAdapter(
            mainActivity,
            mainActivity.viewModel.dayLiveData.value!!,
            mainActivity.routineViewModel.routineLivData.value!!,
            binding.slideFrame,
            binding.slideLayout,
        )
    }
}