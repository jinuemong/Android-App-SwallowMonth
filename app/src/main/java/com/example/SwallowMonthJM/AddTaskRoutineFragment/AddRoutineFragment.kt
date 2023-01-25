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
import com.example.SwallowMonthJM.Server.MasterApplication
import com.example.SwallowMonthJM.databinding.FragmentAddRoutineBinding
import com.google.android.material.tabs.TabLayoutMediator


class AddRoutineFragment : Fragment() {
    private var _binding:FragmentAddRoutineBinding?= null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    private lateinit var callback : OnBackPressedCallback
    private lateinit var fm : FragmentManager
    private lateinit var fragmentPagerAdapter:FragmentAdapter
    private lateinit var monthDataManager: MonthDataManager
    private val tabText = arrayOf(
        "step1", "step2"
    )
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        mainActivity.addViewModel.addType = "routine"
        monthDataManager = MonthDataManager((mainActivity.application as MasterApplication))

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
        view.startAnimation(mainActivity.aniList[1])
        initView()
        setUpListener()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.addViewModel.reset()
        _binding = null
    }
    private fun initView(){
        binding.addRoutineSelectIcon.apply {
            adapter = IconAdapter(mainActivity).apply {
                setOnItemClickListener(object :IconAdapter.OnItemClickListener{
                    override fun onItemClick(iconIndex: Int) {
                        mainActivity.addViewModel.iconType=iconIndex
                        mainActivity.addViewModel.routineChange.value =
                            mainActivity.addViewModel.routineChange.value != true
                    }

                })
            }
        }
        initFragmentAdapter()
        initViewPager()
        initTabLayout()
    }

    private fun setUpListener(){
        binding.addRoutineBack.setOnClickListener {
            mainActivity.onFragmentGoBack(this@AddRoutineFragment)
        }
        binding.routineCommit.setOnClickListener {
            val data = mainActivity.addViewModel.getRoutineData()
            if (data != null) {
                //만약 month id 가 null이라면 새로운 month 데이터 생성
                if (mainActivity.viewModel.monthData.monthId == null) {
                    monthDataManager.addMonthData(mainActivity.viewModel.monthData, paramFun = {
                            if (it != null) {
                                mainActivity.viewModel.monthData = it
                            }
                    })
                }

                //후처리 코드 블럭
                data.monthId = mainActivity.viewModel.monthData.monthId!!
                data.userId = mainActivity.viewModel.profile.userName
//                        mainActivity.routineViewModel.addRoutineData(data)
                mainActivity.onFragmentGoBack(this@AddRoutineFragment)

            }
        }
    }

    private fun initFragmentAdapter(){
        fragmentPagerAdapter= FragmentAdapter(mainActivity)
        fragmentPagerAdapter.addFragment(SelectCalendarFragment())
        fragmentPagerAdapter.addFragment(RoutineSupportFragment())
    }

    private fun initViewPager(){
        binding.addRoutineViewpager.adapter = fragmentPagerAdapter
        binding.addRoutineViewpager.registerOnPageChangeCallback(object :
        ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }

    private fun initTabLayout(){
        binding.addRoutineTabLayout.tabTextColors = mainActivity.tintColor
        TabLayoutMediator(binding.addRoutineTabLayout,binding.addRoutineViewpager)
        {tab,position->
            tab.text = tabText[position]
        }.attach()
    }

}