package com.jinuemong.SwallowMonthJM.DetailView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.jinuemong.SwallowMonthJM.Calendar.CalendarAdapterRoutine
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Model.Routine
import com.jinuemong.SwallowMonthJM.Unit.SelectIconDialog
import com.jinuemong.SwallowMonthJM.Unit.calendarIcon
import com.jinuemong.SwallowMonthJM.databinding.FragmentDetailRoutineBinding


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
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
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
        view.startAnimation(mainActivity.aniList[1])
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
            mainActivity.onFragmentGoBack(this@DetailRoutineFragment)
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