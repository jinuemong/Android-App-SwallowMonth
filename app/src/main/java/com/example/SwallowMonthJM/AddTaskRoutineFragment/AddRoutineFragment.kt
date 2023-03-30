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
import com.example.SwallowMonthJM.Model.MonthData
import com.example.SwallowMonthJM.Model.Routine
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
        monthDataManager = MonthDataManager((mainActivity.masterApp))

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
                //만약 keyDate가 존재하지 않는다면 새로 생성
                monthDataManager.getKeyDateMonthData(mainActivity.userName,mainActivity.addViewModel.keyData,
                paramFun = { findMonth,success->
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
                            val monthData = MonthData(null,mainActivity.userName,
                                mainActivity.addViewModel.keyData,
                                0,0,0,
                                0,0,0)

                            monthDataManager.addMonthData(monthData, paramFun = {
                                if (it != null) {
                                    mainActivity.addViewModel.monthId = it.monthId!!

                                    //만약 현재 데이터도 null이고 keyDate가 같다면 갱신
                                    if (mainActivity.viewModel.monthData.monthId==null
                                        &&mainActivity.viewModel.monthData.keyDate==it.keyDate){
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
    inner class AddThread(private val data: Routine): Thread(){
        override fun run() {
            super.run()
            try {
                sleep(500)
                // 데이터 null인지 확인 -1?
                if (mainActivity.addViewModel.monthId!=-1) {
                    data.monthId = mainActivity.addViewModel.monthId
                    data.userId = mainActivity.viewModel.myProfile.userName
                    mainActivity.routineViewModel.addRoutineData(data)
                    mainActivity.addViewModel.reset()
                    mainActivity.onFragmentGoBack(this@AddRoutineFragment)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}