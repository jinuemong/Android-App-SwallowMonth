package com.example.SwallowMonthJM

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import com.example.SwallowMonthJM.DetailView.RecentlyMonthFragment
import com.example.SwallowMonthJM.MainFragment.FragmentRoutineList
import com.example.SwallowMonthJM.MainFragment.FragmentStatistics
import com.example.SwallowMonthJM.MainFragment.FragmentActivityList
import com.example.SwallowMonthJM.MainFragment.FragmentUserUI
import com.example.SwallowMonthJM.Manager.MonthDataManager
import com.example.SwallowMonthJM.Manager.UserManager
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Server.MasterApplication
import com.example.SwallowMonthJM.Relation.AlarmFragment
import com.example.SwallowMonthJM.Unit.MessageBox
import com.example.SwallowMonthJM.Unit.getPhotoUrl
import com.example.SwallowMonthJM.ViewModel.*
import com.example.SwallowMonthJM.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var masterApp: MasterApplication
    private lateinit var alarmManager: AlarmManager


    lateinit var slideFrame : SlidingUpPanelLayout
    lateinit var slideLayout: View

    val viewModel: MainViewModel by viewModels()
    val addViewModel: AddTaskRoutineViewModel by viewModels()
    val multiPartViewModel : MultiPartViewModel by viewModels()
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
    lateinit var nextFrag : String
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

    companion object{
        const val REQUEST_CODE_1 = 100
        const val REQUEST_CODE_2 = 101
    }
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        supportFragmentManager.fragmentFactory = FragmentFactory()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        frManger = this@MainActivity.supportFragmentManager
        // 레트로핏 생성 //
        masterApp = this@MainActivity.application as MasterApplication
        masterApp.createRetrofit(this@MainActivity)

        // user profile 세팅
        userName = intent.getStringExtra("username").toString()
        if (userName=="null"){
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
        }
        nextFrag = intent.getStringExtra("fragment").toString()
        Log.d("nextFrag",nextFrag)
        //슬라이딩 뷰
        slideFrame = binding.mainFrame
        slideLayout = binding.slideLayout

        //애니메이션 설정
        aniList = arrayOf(
            AnimationUtils.loadAnimation(this@MainActivity, R.anim.enter_left),
            AnimationUtils.loadAnimation(this@MainActivity, R.anim.enter_up),
            AnimationUtils.loadAnimation(this@MainActivity, R.anim.enter_down),
            AnimationUtils.loadAnimation(this@MainActivity, R.anim.wave),
            AnimationUtils.loadAnimation(this@MainActivity,R.anim.enter_right)
        )

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


        //뷰 초기화®
        initView()

//
//        setAlarm(resetDayIntent(),resetMonthIntent())
//        setAlarm(dayBroadIntent(),monthBroadIntent())
    }

    private fun initView() {
        UserManager(masterApp,this@MainActivity)
            .getUserProfileWithUserName(userName, paramFun = { profile,_->
                if (profile!=null) {
                    viewModel.myProfile = profile
                    setProfile(profile)
                }

                //현재 데이터 설정
                initCurrentDate()
                //지연 데이터 적용
                Thread.sleep(500)
                initFragmentAdapter()
                initViewPager()
                initTabLayout()
                setUpListener()

                // 만약 month DATA 갱신 -> 최근 업적 보여줌
                if (nextFrag=="recently"){
                    val recentlyBox = RecentlyMonthFragment()
                    Log.d("reset recently",recentlyBox.toString())
                    recentlyBox.show(frManger,null)
                }
            })
    }

    private fun initCurrentDate() {
        val date = Calendar.getInstance().time
        val dateMonth: Int = SimpleDateFormat("MM", Locale.KOREA).format(date).toInt()
        viewModel.apply {
            monthDataManager = MonthDataManager(masterApp)
            todayMonth = dateMonth
            setCurrentData(date,this@MainActivity)
        }
        viewPager = binding.mainMidViewpager

    }

    private fun setUpListener(){
        binding.addTaskButton.setOnClickListener {
            onFragmentChange(AddTaskFragment())
        }
        binding.addTodayTask.setOnClickListener {
            onFragmentChange(AddTodayTaskFragment())
        }

        binding.alarmView.setOnClickListener {
            onFragmentChange(AlarmFragment.newInstance(userName))
        }
    }

    private fun initFragmentAdapter() {
        fragmentPageAdapter = FragmentAdapter(this@MainActivity)
        fragmentPageAdapter.apply {
            addFragment(FragmentActivityList())
            addFragment(FragmentStatistics())
            addFragment(Fragment())
            addFragment(FragmentRoutineList())
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
        viewPager.offscreenPageLimit = 4
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

    fun setProfile(profile: Profile){
        binding.apply {
            mainTopName.text = profile.userName
            mainTopComment.text = profile.userComment
            Glide.with(this@MainActivity)
                .load(getPhotoUrl(profile.userImage,masterApp.baseUrl))
                .into(mainTopImage)

        }
    }

//    private fun setAlarm(dayIntent:PendingIntent,monthIntent: PendingIntent){
//
//        val calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY,18)
//            set(Calendar.MINUTE,33)
//            set(Calendar.SECOND,0)
//            set(Calendar.MILLISECOND,0)
//
//        }
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        val intent =
//            if (day==19){
//                monthIntent
//            }else{
//                dayIntent
//            }
//        if (calendar.time<Date()){// 설정한 시간에 따라서 알람이 안될 수도 있음
//            calendar.add(Calendar.MINUTE,1)
//        }
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            AlarmManager.INTERVAL_DAY,
//            intent
//        )
//
//    }



//    //데이 엑티비티 리셋
//    private fun resetDayIntent() : PendingIntent{
//        return Intent(this@MainActivity,
//            MainActivity::class.java)
//            .let {intent->
//                intent.putExtra("username",userName)
//                intent.putExtra("fragment","not")
//                PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
//            }
//    }
//
//    //month 액티비티 리셋
//    private fun resetMonthIntent() : PendingIntent{
//        return Intent(this@MainActivity,
//        MainActivity::class.java)
//            .let { intent->
//                intent.putExtra("username",userName)
//                intent.putExtra("fragment","recently")
//                PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
//            }
//    }
//
//    //앱 실행 알림
//    private fun dayBroadIntent() : PendingIntent{
//        return Intent(this@MainActivity,AlarmBroadCastReceiver::class.java)
//            .let {intent->
//                intent.putExtra("code", REQUEST_CODE_1)
//                intent.putExtra("username",userName)
//                PendingIntent.getBroadcast(this@MainActivity,REQUEST_CODE_1,intent,PendingIntent.FLAG_IMMUTABLE)
//            }
//    }
//
//    private fun monthBroadIntent() : PendingIntent{
//        return Intent(this@MainActivity,AlarmBroadCastReceiver::class.java)
//            .let { intent ->
//                intent.putExtra("code", REQUEST_CODE_2)
//                intent.putExtra("username", userName)
//                PendingIntent.getBroadcast(this@MainActivity,REQUEST_CODE_2,intent,PendingIntent.FLAG_IMMUTABLE)
//            }
//    }

}

