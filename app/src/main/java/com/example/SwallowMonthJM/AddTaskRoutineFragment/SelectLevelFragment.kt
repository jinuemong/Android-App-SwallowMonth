package com.example.SwallowMonthJM.AddTaskRoutineFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentTaskTwoBinding


class SelectLevelFragment : Fragment() {
    private var _binding:FragmentTaskTwoBinding?=null
    private val binding get() = _binding!!
    private var selectedNum = -1
    private var layoutList = ArrayList<LinearLayout>()
    private lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity =  context as MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskTwoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpListener()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(){

        layoutList.apply {
            add(binding.level1)
            add(binding.level2)
            add(binding.level3)
            add(binding.level4)
            add(binding.level5)
        }

    }

    private fun setUpListener(){
            for (i in 0 until layoutList.size){
            layoutList[i].setOnClickListener {
                if (selectedNum != -1) {
                    layoutList[selectedNum].setBackgroundResource(R.drawable.round_border)
                }
                layoutList[i].setBackgroundResource(R.color.color_type3)
                selectedNum = i
                mainActivity.addViewModel.level = selectedNum
            }
        }
    }
}