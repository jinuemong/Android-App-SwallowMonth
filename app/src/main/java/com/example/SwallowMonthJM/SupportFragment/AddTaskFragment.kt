package com.example.SwallowMonthJM.SupportFragment

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
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.FragmentAddTaskBinding
import com.google.android.material.tabs.TabLayoutMediator

class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding?= null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    private lateinit var callback : OnBackPressedCallback
    private lateinit var fm : FragmentManager
    private lateinit var fragmentPagerAdapter: FragmentAdapter

    private val tabText = arrayOf(
        "step1", "step2"
    )
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
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
        view.startAnimation(mainActivity.aniList[0])
        initView()
        setUpListener()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun initView(){
        initFragmentAdapter()
        initViewPager()
        initTabLayout()
    }
    private fun setUpListener(){

    }
    private fun initFragmentAdapter(){
        fragmentPagerAdapter= FragmentAdapter(mainActivity)
        fragmentPagerAdapter.addFragment(TaskFragmentOne())
        fragmentPagerAdapter.addFragment(TaskFragmentTwo())
    }
    private fun initViewPager(){
        binding.addTaskViewpager.adapter = fragmentPagerAdapter
        binding.addTaskViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }

    private fun initTabLayout(){
        binding.addTaskTabLayout.tabTextColors = mainActivity.tintColor
        TabLayoutMediator(binding.addTaskTabLayout,binding.addTaskViewpager)
        {tab,position->
            tab.text = tabText[position]
        }.attach()
    }

}