package com.example.SwallowMonthJM

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.Adapter.FragmentAdapter
import com.example.SwallowMonthJM.AddTaskRoutineFragment.AddTaskFragment
import com.example.SwallowMonthJM.AddTaskRoutineFragment.AddTodayTaskFragment
import com.example.SwallowMonthJM.MainFragment.FragmentRepeatTaskList
import com.example.SwallowMonthJM.MainFragment.FragmentStatistics
import com.example.SwallowMonthJM.MainFragment.FragmentTaskList
import com.example.SwallowMonthJM.MainFragment.FragmentUserUI
import com.example.SwallowMonthJM.Manager.MonthDataManager
import com.example.SwallowMonthJM.Manager.UserManager
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.ViewModel.AddTaskRoutineViewModel
import com.example.SwallowMonthJM.ViewModel.MainViewModel
import com.example.SwallowMonthJM.ViewModel.RoutineViewModel
import com.example.SwallowMonthJM.ViewModel.TaskViewModel
import com.example.SwallowMonthJM.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()
    val addViewModel: AddTaskRoutineViewModel by viewModels()
    val routineViewModel by lazy {
        ViewModelProvider(this@MainActivity,RoutineViewModel
            .Factory(this@MainActivity))[RoutineViewModel::class.java]
    }

    val taskViewModel  by lazy {
        ViewModelProvider(this@MainActivity,TaskViewModel
            .Factory(this@MainActivity))[TaskViewModel::class.java]
    }
    lateinit var frManger: FragmentManager
    private var backPressTime:Long = 0
    private lateinit var callback: OnBackPressedCallback
    private lateinit var fragmentPageAdapter: FragmentAdapter
    lateinit var viewPager: ViewPager2
    lateinit var userName:String
    //click tab
    val tintColor = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_selected)
        ),
        intArrayOf(Color.parseColor("#629CD1"), Color.parseColor("#2C608F"))
    )

    //tab Icon
    private val iconView = arrayOf(
        R.drawable.ic_baseline_home_24,
        R.drawable.ic_iconmonstr_bar_chart_thin,
        R.drawable.ic_add_box_24,
        R.drawable.ic_iconmonstr_refresh_7,
        R.drawable.ic_iconmonstr_user_male_thin
    )
    lateinit var aniList: Array<Animation>
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = FragmentFactory()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        frManger = this@MainActivity.supportFragmentManager

        // user profile 세팅
        userName = intent.getStringExtra("username").toString()


        //애니메이션 설정
        aniList = arrayOf(
            AnimationUtils.loadAnimation(this@MainActivity, R.anim.enter_left),
            AnimationUtils.loadAnimation(this@MainActivity, R.anim.enter_up),
            AnimationUtils.loadAnimation(this@MainActivity, R.anim.enter_down),
            AnimationUtils.loadAnimation(this@MainActivity, R.anim.wave),
            AnimationUtils.loadAnimation(this@MainActivity,R.anim.enter_right)
        )
        initView()
        setUpListener()

        //뒤로가기 조작 (2초 이내 연속 클릭 시 종료)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis()>backPressTime+2000){
                    backPressTime = System.currentTimeMillis()
                    Toast.makeText(applicationContext,"'뒤로' 버틀을 한번 더 누르시면 앱이 종료됩니다."
                        ,Toast.LENGTH_SHORT).show()
                }else{
                    finishAffinity()
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this,callback)
    }

    private fun initView() {
        UserManager((this@MainActivity.application as MasterApplication))
            .getUserProfile(userName, paramFun = { profile->
                viewModel.profile = profile
                binding.apply {
                    mainTopName.text = profile.userName
                    mainTopComment.text = profile.userComment
                    Glide.with(this@MainActivity)
                        .load(profile.userImage)
                        .into(mainTopImage)
                }
                //현재 데이터 설정
                initCurrentDate()
            })
    }

    private fun initCurrentDate() {
        val date = Calendar.getInstance().time
        val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()
        viewModel.apply {
            monthDataManager = MonthDataManager(application as MasterApplication)
            todayMonth = dateMonth
            setCurrentData(date,this@MainActivity)
        }
        viewPager = binding.mainMidViewpager
        initFragmentAdapter()
        initViewPager()
        initTabLayout()
    }

    private fun setUpListener(){
        binding.addTaskButton.setOnClickListener {
            onFragmentChange(AddTaskFragment())
        }
        binding.addTodayTask.setOnClickListener {
            onFragmentChange(AddTodayTaskFragment())
        }

    }

    private fun initFragmentAdapter() {
        fragmentPageAdapter = FragmentAdapter(this@MainActivity)
        fragmentPageAdapter.apply {
            addFragment(FragmentTaskList())
            addFragment(FragmentStatistics())
            addFragment(Fragment())
            addFragment(FragmentRepeatTaskList())
            addFragment(FragmentUserUI())
        }
    }

    private fun initViewPager() {
        viewPager.adapter = fragmentPageAdapter
        viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
        //스크롤 막기
        viewPager.isUserInputEnabled = false
    }

    private fun initTabLayout() {
        binding.mainBottomTabLayout.tabIconTint = tintColor
        TabLayoutMediator(binding.mainBottomTabLayout, viewPager)
        { tab, position ->
            tab.setIcon(iconView[position])
        }.attach()
        val tab = binding.mainBottomTabLayout.getChildAt(0) as ViewGroup
        val tabView = tab.getChildAt(2)
        tabView.visibility = View.INVISIBLE
    }

    fun onFragmentChange(goFragment: Fragment) {
        frManger.beginTransaction().replace(R.id.view_container_in_main, goFragment)
            .addToBackStack(null)
            .commit()
    }

    fun onFragmentGoBack(fragment: Fragment) {
        frManger.beginTransaction().remove(fragment).commit()
        frManger.popBackStack()
    }

}

