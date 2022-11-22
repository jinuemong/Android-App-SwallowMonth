package com.example.SwallowMonthJM.SupportFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.SwallowMonthJM.Adapter.IconAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.FragmentAddRoutineBinding


class AddRoutineFragment : Fragment() {
    private var _binding:FragmentAddRoutineBinding?= null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    private lateinit var callback : OnBackPressedCallback
    private lateinit var fm : FragmentManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        fm = (activity as MainActivity).supportFragmentManager
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@AddRoutineFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddRoutineBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpListener()
    }

    private fun initView(){
        binding.addRoutineSelectIcon.apply {
            adapter = IconAdapter()
        }
    }

    private fun setUpListener(){
        binding.addRoutineBack.setOnClickListener {
            mainActivity.onFragmentGoBack(this@AddRoutineFragment)
        }
    }

}