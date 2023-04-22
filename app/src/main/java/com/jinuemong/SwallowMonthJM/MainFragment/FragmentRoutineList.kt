package com.jinuemong.SwallowMonthJM.MainFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jinuemong.SwallowMonthJM.Adapter.RoutineListAdapter
import com.jinuemong.SwallowMonthJM.AddTaskRoutineFragment.AddRoutineFragment
import com.jinuemong.SwallowMonthJM.DetailView.DetailRoutineFragment
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Model.Routine
import com.jinuemong.SwallowMonthJM.databinding.FragmentRepeatTaskListBinding

class FragmentRoutineList : Fragment() {
    private var _binding:FragmentRepeatTaskListBinding?=null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity
    lateinit var adapter: RoutineListAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRepeatTaskListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpListener()
    }


    private fun initView(){


        adapter = RoutineListAdapter(mainActivity,mainActivity.routineViewModel.routineLivData.value!!)
        //어댑터 넣고 클릭 리스너 적용
        binding.routineRecycler.adapter = adapter.apply {
            setOnItemClickListener(object : RoutineListAdapter.OnItemClickListener{
                override fun onClick(item: Routine) {
                    mainActivity.onFragmentChange(DetailRoutineFragment(item))
                }

            })
        }
        mainActivity.routineViewModel.routineLivData.observe(mainActivity, Observer {
            (binding.routineRecycler.adapter as RoutineListAdapter).setData(it)
        })

    }

    private fun setUpListener(){
        binding.routineAddButton.setOnClickListener {
            mainActivity.onFragmentChange(AddRoutineFragment())
        }
    }

}