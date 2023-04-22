package com.jinuemong.SwallowMonthJM.AddTaskRoutineFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.jinuemong.SwallowMonthJM.Adapter.IconAdapter
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Manager.MonthDataManager
import com.jinuemong.SwallowMonthJM.Model.MonthData
import com.jinuemong.SwallowMonthJM.Model.Task
import com.jinuemong.SwallowMonthJM.R
import com.jinuemong.SwallowMonthJM.databinding.FragmentAddTodayTaskBinding


class AddTodayTaskFragment : Fragment() {
    private var _binding: FragmentAddTodayTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private var selectedNum = -1
    private var layoutList = ArrayList<LinearLayout>()
    private lateinit var callback: OnBackPressedCallback
    private lateinit var monthDataManager: MonthDataManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        mainActivity.addViewModel.addType = "task"
        monthDataManager = MonthDataManager((mainActivity.masterApp))

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@AddTodayTaskFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTodayTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startAnimation(mainActivity.aniList[1])
        initView()
        setUpListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mainActivity.addViewModel.reset()
    }

    private fun initView() {
        mainActivity.addViewModel.startNum = mainActivity.viewModel.todayCalPosition
        mainActivity.addViewModel.keyData = mainActivity.viewModel.todayKeyDate
        //아이콘 선택
        binding.addTaskSelectIcon.apply {
            adapter = IconAdapter(mainActivity).apply {
                setOnItemClickListener(object : IconAdapter.OnItemClickListener {
                    override fun onItemClick(iconIndex: Int) {
                        mainActivity.addViewModel.iconType = iconIndex
                    }
                })
            }
        }

        binding.addTodayEdit.setOnKeyListener { _, keyCode, event ->
            var handled = false
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                addTypeData()
                handled = true
            }
            binding.addTodayButton.setOnClickListener {
                addTypeData()
                handled = true
            }
            handled
        }

        initLevelView()
    }

    private fun setUpListener() {
        binding.commitButton.setOnClickListener {
            val data = mainActivity.addViewModel.getTaskData()
            if (data != null) {
                //만약 keyDate가 존재하지 않는다면 새로 생성
                monthDataManager.getKeyDateMonthData(mainActivity.userName,
                    mainActivity.addViewModel.keyData,
                    paramFun = { findMonth, success ->
                        if (success) {
                            val monthId = if (findMonth!=null && findMonth.size>0){
                                findMonth[0].monthId
                            }else{
                                null
                            }
                            // null이 아니면 해당 위치에 데이터 추가
                            if (monthId != null) {
                                mainActivity.addViewModel.monthId = monthId

                                //null 경우 새로 생성
                            } else {
                                val monthData = MonthData(
                                    null, mainActivity.userName,
                                    mainActivity.addViewModel.keyData,
                                    0, 0, 0,
                                    0, 0, 0
                                )

                                monthDataManager.addMonthData(monthData, paramFun = {
                                    if (it != null) {
                                        mainActivity.addViewModel.monthId = it.monthId!!

                                        //만약 현재 데이터도 null이고 keyDate가 같다면 갱신
                                        if (mainActivity.viewModel.monthData.monthId == null
                                            && mainActivity.viewModel.monthData.keyDate == it.keyDate
                                        ) {
                                            mainActivity.viewModel.monthData = it
                                        }
                                    }
                                })
                            }
                        }
                    })
                //후처리 코드
                val startThread = AddThread(data)
                startThread.start()
            }
        }
        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@AddTodayTaskFragment)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun addTypeData() {
        binding.addTodayEdit.apply {
            if (this.text != null && this.text.toString() != "") {
                mainActivity.addViewModel.text = this.text.toString()
                this.setBackgroundColor(R.color.gray)
            }
        }
        //바 내리기
        val imm =
            mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainActivity.currentFocus?.windowToken, 0)
    }

    private fun initLevelView() {
        val levelLayout = binding.selectLevelBox
        layoutList.apply {
            add(levelLayout.level1)
            add(levelLayout.level2)
            add(levelLayout.level3)
            add(levelLayout.level4)
            add(levelLayout.level5)
        }
        for (i in 0 until layoutList.size) {
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

    inner class AddThread(private val data: Task) : Thread() {
        val startNum = mainActivity.addViewModel.startNum
        var endNum = mainActivity.addViewModel.endNum
        override fun run() {
            super.run()
            try {
                sleep(500)
                if (endNum == -1) {
                    endNum = startNum
                }
                if (mainActivity.viewModel.monthData.monthId != -1) {
                    data.monthId = mainActivity.addViewModel.monthId
                    data.userId = mainActivity.viewModel.myProfile.userName
                    mainActivity.taskViewModel.addTaskData(startNum, endNum, data)
                    mainActivity.addViewModel.reset()
                    mainActivity.onFragmentGoBack(this@AddTodayTaskFragment)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}