package com.jinuemong.SwallowMonthJM.DetailView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Manager.MonthDataManager
import com.jinuemong.SwallowMonthJM.UIFragment.RecordFragment
import com.jinuemong.SwallowMonthJM.databinding.FragmentRecentlyMonthBinding


class RecentlyMonthFragment : DialogFragment() {
    private var _binding : FragmentRecentlyMonthBinding? = null
    private val binding get()=  _binding!!
    private lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecentlyMonthBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MonthDataManager(mainActivity.masterApp)
            .getRecentlyData(mainActivity.userName, paramFun = {data->
                if (data!=null){
                    binding.recordBox.keyDate.text = data.keyDate
                    binding.recordBox.taskListPer.progress = data.totalPer
                    binding.recordBox.taskListPerText.text = "${data.totalPer}%"
                    binding.recordBox.subject.text = "${data.userId}'s Record Card"
                    binding.recordBox.totalActivity.text = "${data.clearNum} / ${data.activityNum}"
                    binding.recordBox.totalPointMent.text = "${data.totalPoint} point !"
                    binding.recordBox.userRanking.text = data.ranking.toString()

                }else{
                    this@RecentlyMonthFragment.dismissNow()
                }
            })
        binding.notButton.setOnClickListener {
            this@RecentlyMonthFragment.dismissNow()
        }

        binding.commitButton.setOnClickListener {
            mainActivity.onFragmentChange(RecordFragment.newInstance(mainActivity.userName))
            this@RecentlyMonthFragment.dismissNow()
        }
    }

}