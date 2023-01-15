package com.example.SwallowMonthJM.AddTaskRoutineFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.SwallowMonthJM.Adapter.IconAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.FragmentAddTodayTaskBinding


class AddTodayTaskFragment : Fragment() {
    private var _binding :FragmentAddTodayTaskBinding?= null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        mainActivity.addViewModel.addType = "task"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTodayTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView(){
        mainActivity.viewModel.currentDayPosition
        //아이콘 선택
        binding.addTaskSelectIcon.apply {
            adapter = IconAdapter(mainActivity).apply {
                setOnItemClickListener(object : IconAdapter.OnItemClickListener{
                    override fun onItemClick(iconIndex: Int) {
                        mainActivity.addViewModel.iconType = iconIndex
                    }
                })
            }
        }
    }
}