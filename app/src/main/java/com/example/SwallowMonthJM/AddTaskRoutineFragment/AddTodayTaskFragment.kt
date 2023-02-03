package com.example.SwallowMonthJM.AddTaskRoutineFragment

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
import com.example.SwallowMonthJM.Adapter.IconAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.MonthDataManager
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentAddTodayTaskBinding


class AddTodayTaskFragment : Fragment() {
    private var _binding :FragmentAddTodayTaskBinding?= null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private var selectedNum = -1
    private var layoutList = ArrayList<LinearLayout>()
    private lateinit var callback : OnBackPressedCallback
    private lateinit var monthDataManager: MonthDataManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        mainActivity.addViewModel.addType = "task"
        monthDataManager = MonthDataManager((mainActivity.application as MasterApplication))

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
        _binding = FragmentAddTodayTaskBinding.inflate(inflater,container,false)
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

    private fun initView(){
        mainActivity.addViewModel.startNum = mainActivity.viewModel.todayDayPosition
        mainActivity.addViewModel.keyData = mainActivity.viewModel.currentDate.keyDate
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

        binding.addTodayEdit.setOnKeyListener { v, keyCode, event ->
            var handled=false
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

    private fun setUpListener(){
        binding.commitButton.setOnClickListener {
            val data = mainActivity.addViewModel.getTaskData()
            if (data!=null){
                //만약에 month id 가 null이라면 새로운 month 데이터 생성
                if (mainActivity.viewModel.monthData.monthId == null) {
                    monthDataManager.addMonthData(mainActivity.viewModel.monthData, paramFun = {
                        if (it != null) {
                            mainActivity.viewModel.monthData = it
                        }
                    })
                }
                //후처리 코드
                val startIntroThread = AddThread(data)
                startIntroThread.start()
            }
        }
        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@AddTodayTaskFragment)
        }
    }
    private fun addTypeData(){
        binding.addTodayEdit.apply {
            if (this.text!=null && this.text.toString()!="") {
                mainActivity.addViewModel.text = this.text.toString()
                this.setText("")
            }
        }
        //바 내리기
        val imm =
            mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainActivity.currentFocus?.windowToken, 0)
    }
    private fun initLevelView(){
        val levelLayout = binding.selectLevelBox
        layoutList.apply {
            add(levelLayout.level1)
            add(levelLayout.level2)
            add(levelLayout.level3)
            add(levelLayout.level4)
            add(levelLayout.level5)
        }
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

    inner class AddThread(private val data: Task) : Thread(){
        val startNum = mainActivity.addViewModel.startNum
        var endNum = mainActivity.addViewModel.endNum
        override fun run() {
            super.run()
            try {
                sleep(500)
                if (endNum==-1) {
                    endNum=startNum
                }
                mainActivity.viewModel.monthData.monthId.let {
                    if(it!=null){
                        data.monthId = it
                        data.userId = mainActivity.viewModel.profile.userName
                        mainActivity.taskViewModel.addTaskData(startNum, endNum, data)
                        mainActivity.onFragmentGoBack(this@AddTodayTaskFragment)
                    }
                }
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
        }
    }
}