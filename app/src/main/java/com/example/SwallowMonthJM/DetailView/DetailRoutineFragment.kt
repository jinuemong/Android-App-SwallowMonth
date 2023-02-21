package com.example.SwallowMonthJM.DetailView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.SwallowMonthJM.Calendar.CalendarAdapterRoutine
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.SelectIconDialog
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.FragmentDetailRoutineBinding
import org.mozilla.javascript.tools.jsc.Main


class DetailRoutineFragment(
    private val routine:Routine,
) : Fragment() {
    private var _binding : FragmentDetailRoutineBinding?= null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var callback:OnBackPressedCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        callback = object  : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@DetailRoutineFragment)
            }

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailRoutineBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        binding.routineName.text = routine.text
        binding.routineIcon.setImageResource(calendarIcon[routine.iconType])
        binding.routineMainText.text ="${routine.clearRoutine} / ${routine.totalRoutine}"
        binding.topTextRoutine.text = routine.topText
        initCalendar()
        setUpListener()
    }

    private fun setUpListener(){

        //아이콘 변경
        binding.routineIcon.setOnClickListener {
            val dig = SelectIconDialog(mainActivity)
            dig.showDig()
            dig.setOnClickedListener(object : SelectIconDialog.ButtonClickListener {
                override fun onClicked(index: Int?) {
                    if (index != null) {
                        mainActivity.routineViewModel.setRoutineIcon(index, routine)
                        binding.routineIcon.setImageResource(calendarIcon[index])
                    }
                }

            })
        }

        //삭제 버튼
        binding.delButton.setOnClickListener {
            mainActivity.routineViewModel.delRoutineData(routine)
        }

        //뒤로가기 버튼
        binding.closeButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@DetailRoutineFragment)
        }
    }

    private fun initCalendar(){
        binding.calendarLayout.fragCalenderRecycler
            .adapter = CalendarAdapterRoutine(
            mainActivity,binding.calendarLayout.fragCalenderLinear,routine
        )
    }
}