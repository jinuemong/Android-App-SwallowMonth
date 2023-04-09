package com.example.SwallowMonthJM.TaskFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.SwallowMonthJM.Adapter.TaskListAdapter
import com.example.SwallowMonthJM.Adapter.TodayRoutineAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.DetailView.RoutineSlider
import com.example.SwallowMonthJM.DetailView.TaskSlider
import com.example.SwallowMonthJM.MainFragment.FragmentActivityList
import com.example.SwallowMonthJM.databinding.FragmentTaskBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout


class TaskFragment() : Fragment() {
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    var taskListAdapter: TaskListAdapter? = null
    var routineListAdapter: TodayRoutineAdapter? = null
    private lateinit var slideFrame: SlidingUpPanelLayout
    private lateinit var slideLayout: View
    private lateinit var taskSlider: View
    private lateinit var routineSlider: View

    override fun onAttach(context: Context) {
        Log.d("taskfragment","onAttach")
        super.onAttach(context)
        mainActivity = context as MainActivity
        slideFrame = mainActivity.slideFrame
        slideLayout = mainActivity.slideLayout
        taskSlider = slideLayout.findViewById(R.id.slide_task)
        routineSlider = slideLayout.findViewById(R.id.slide_routine)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    override fun onResume() {
        super.onResume()

    }

    fun initView() {
        //루틴 어댑터 초기화
        initRoutineAdapter()
        //task 어댑터 초기화
        initTaskAdapter()



        //루틴 데이터 변화 관찰
        mainActivity.routineViewModel.routineLivData.observe(mainActivity, Observer {
            routineListAdapter?.setData(it)
        })

        //task 데이터 변화 관찰
        mainActivity.taskViewModel.currentTaskList.observe(mainActivity, Observer {
            taskListAdapter?.setData(it)
        })

        // 날짜 데이터 변경 시 적용
        mainActivity.viewModel.currentDayPosition.observe(mainActivity, Observer { dayIndex ->
            //현재 날짜 인덱스의 task 갱신
            taskListAdapter?.setDayDate(dayIndex)

            //루틴 리싸이클러의 현재 날짜 인덱스 변경
            routineListAdapter?.setDayDate(dayIndex)
        })

    }

    private fun initTaskAdapter() {
        //task 뷰 init
        //어댑터 생성
        taskListAdapter = TaskListAdapter(
            mainActivity, mainActivity.taskViewModel.currentTaskList.value!!,
            mainActivity.viewModel.currentDayPosition.value!!,false)
            //어댑터 내부 클릭 이벤트 적용
            .apply {
                setOnItemClickListener(object : TaskListAdapter.OnItemClickListener {
                    override fun onItemClick(task: Task) {
                        routineSlider.visibility = View.GONE
                        taskSlider.apply {
                            visibility = View.VISIBLE
                            TaskSlider(this, slideFrame, mainActivity, task)
                                .apply { initSlide() }
                            slideFrame.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                        }
                    }
                })
            }
        //어댑터 부착
        binding.taskView.adapter = taskListAdapter
    }

    private fun initRoutineAdapter() {
        //류틴 뷰 init
        //어댑터 생성
        routineListAdapter = TodayRoutineAdapter(
            mainActivity,
            mainActivity.routineViewModel.routineLivData.value!!,
            mainActivity.viewModel.currentDayPosition.value!!,
            false
        )
            // 어댑터 내부 클릭 이벤트 적용
            .apply {
                setOnItemClickListener(object : TodayRoutineAdapter.OnItemClickListener {
                    override fun onItemClick(dayPosition: Int, routine: Routine) {
                        taskSlider.visibility = View.GONE
                        routineSlider.apply {
                            visibility = View.VISIBLE
                            RoutineSlider(this, slideFrame, mainActivity, dayPosition, routine)
                                .apply { initSlide() }
                            slideFrame.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                        }
                    }
                })
            }
        // 어댑터 부착
        binding.routineView.adapter = routineListAdapter
    }



}