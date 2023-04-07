package com.example.SwallowMonthJM.AddTaskRoutineFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.SwallowMonthJM.Calendar.CalendarAdapterAddTask
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.FragmentRoutineBinding

//시작일

class RoutineSupportFragment : Fragment() {
    private var _binding:FragmentRoutineBinding? = null
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
    ): View? {
        _binding = FragmentRoutineBinding.inflate(inflater,container,false)
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

    @SuppressLint("SetTextI18n")
    private fun initView(){
        binding.routineBox.routinePerText.text="0%"
        val model  =mainActivity.addViewModel
        model.routineChange.observe(mainActivity, Observer {
            model.apply {
                if(iconType!=-1){
                    binding.routineBox.routineIcon.setImageResource(calendarIcon[iconType])
                }
                if(cycle!=99){
                    binding.routineBox.routineCycle.text = topText
                }
                if(totalRoutine!=-1){
                    binding.routineBox.totalRoutine.text = "0 / $totalRoutine"
                }

            }
        })
    }

    private fun setUpListener(){
        binding.routineEditText.setOnKeyListener { _, keyCode, event ->
            var handled=false

            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                addTypeData(binding.routineEditText.text.toString())
                handled = true
            }
            binding.routineAddButton.setOnClickListener {
                addTypeData(binding.routineEditText.text.toString())
                handled = true
            }
            handled
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun addTypeData(text:String){
        if (text!="") {
            mainActivity.addViewModel.text = text
            binding.routineBox.routineText.text=text
            binding.routineEditText.setBackgroundColor(R.color.gray)
        }
        //바 내리기
        val imm =
            mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainActivity.currentFocus?.windowToken, 0)
    }



}