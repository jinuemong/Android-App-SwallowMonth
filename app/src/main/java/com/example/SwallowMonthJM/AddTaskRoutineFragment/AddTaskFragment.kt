package com.example.SwallowMonthJM.AddTaskRoutineFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.SwallowMonthJM.Adapter.FragmentAdapter
import com.example.SwallowMonthJM.Adapter.IconAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.MonthDataManager
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.Server.MasterApplication
import com.example.SwallowMonthJM.Unit.TaskAddSlider
import com.example.SwallowMonthJM.databinding.FragmentAddTaskBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding?= null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    private lateinit var callback : OnBackPressedCallback
    private lateinit var fm : FragmentManager
    private lateinit var fragmentPagerAdapter: FragmentAdapter
    private lateinit var monthDataManager: MonthDataManager

    private val tabText = arrayOf(
        "step1", "step2"
    )
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        mainActivity.addViewModel.addType = "task"
        fm = (activity as MainActivity).supportFragmentManager
        monthDataManager = MonthDataManager((mainActivity.application as MasterApplication))

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@AddTaskFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater,container,false)
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
        binding.addTaskSelectIcon.apply {
            adapter = IconAdapter(mainActivity).apply {
                setOnItemClickListener(object :IconAdapter.OnItemClickListener{
                    override fun onItemClick(iconIndex: Int) {
                        mainActivity.addViewModel.iconType = iconIndex
                        val state = binding.slideFrame.panelState
                        if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                        }
                        else if (state == SlidingUpPanelLayout.PanelState.EXPANDED) {
                            binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                        }
                    }
                })
            }
        }
        initFragmentAdapter()
        initViewPager()
        initTabLayout()
        binding.fragAddLeft.animation = mainActivity.aniList[0]
        binding.slideLayout.apply {
            TaskAddSlider(this,mainActivity, addData = { text ->
                mainActivity.addViewModel.text = text
                val state = binding.slideFrame.panelState
                if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                }
                else if (state == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                }
            }).apply {
                setUpListener()
            }
        }
    }

    private fun setUpListener(){
        binding.addTaskBack.setOnClickListener {
            mainActivity.onFragmentGoBack(this@AddTaskFragment)
        }

        binding.addTaskCommit.setOnClickListener {
            if(mainActivity.addViewModel.getTextData()==""){
                val state = binding.slideFrame.panelState
                if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                }
                else if (state == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                }
            }
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

    }
    private fun initFragmentAdapter(){
        fragmentPagerAdapter= FragmentAdapter(mainActivity)
        fragmentPagerAdapter.addFragment(SelectLevelFragment())
        fragmentPagerAdapter.addFragment(SelectCalendarFragment())
    }

    private fun initViewPager(){
        binding.addTaskViewpager.adapter = fragmentPagerAdapter
        binding.addTaskViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
        //스크롤 막기
        binding.addTaskViewpager.isUserInputEnabled = false
    }

    private fun initTabLayout(){
        binding.addTaskTabLayout.tabTextColors = mainActivity.tintColor
        TabLayoutMediator(binding.addTaskTabLayout,binding.addTaskViewpager)
        {tab,position->
            tab.text = tabText[position]
        }.attach()
    }

    inner class AddThread(val data: Task) : Thread(){
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
                        //                mainActivity.taskViewModel.addTaskData(startNum,endNum,data)
                        mainActivity.onFragmentGoBack(this@AddTaskFragment)
                    }
                }
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
        }
    }
}