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
    private lateinit var taskSlide : TaskAddSlider
    private val tabText = arrayOf(
        "step1", "step2"
    )
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        mainActivity.addViewModel.addType = "task"
        fm = (activity as MainActivity).supportFragmentManager
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
                        binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                    }
                })
            }
        }
        initFragmentAdapter()
        initViewPager()
        initTabLayout()
        binding.fragAddLeft.animation = mainActivity.aniList[0]
        val slideLayout = binding.slideLayout
        taskSlide = TaskAddSlider(slideLayout,mainActivity, addData = { text ->
            mainActivity.addViewModel.text = text
            binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        })
    }

    private fun setUpListener(){
        binding.addTaskBack.setOnClickListener {
            mainActivity.onFragmentGoBack(this@AddTaskFragment)
        }

        binding.addTaskCommit.setOnClickListener {
            if(mainActivity.addViewModel.getTextData()==""){
                binding.slideFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
            }
            val data = mainActivity.addViewModel.getTaskData()
            val startNum = mainActivity.addViewModel.startNum
            var endNum = mainActivity.addViewModel.endNum
            if (endNum==-1) {
                endNum=startNum
            }
            if (data!=null){
                mainActivity.taskViewModel.addTaskData(startNum,endNum,data)
                mainActivity.onFragmentGoBack(this@AddTaskFragment)
            }
        }

        taskSlide.setUpListener()
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

}