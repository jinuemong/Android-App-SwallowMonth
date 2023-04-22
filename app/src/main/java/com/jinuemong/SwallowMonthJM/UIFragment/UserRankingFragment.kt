package com.jinuemong.SwallowMonthJM.UIFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.jinuemong.SwallowMonthJM.Adapter.RankingAdapter
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Manager.MonthDataManager
import com.jinuemong.SwallowMonthJM.Model.RecordData
import com.jinuemong.SwallowMonthJM.databinding.FragmentUserRankingBinding

class UserRankingFragment : Fragment() {
    private var myName = ""
    private var currentKeyDate = ""
    private var _binding : FragmentUserRankingBinding? = null
    private val binding get() = _binding!!
    private var adapter : RankingAdapter? = null
    private lateinit var mainActivity: MainActivity
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        onBackPressedCallback = object  : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@UserRankingFragment)
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myName = it.getString("myName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding = FragmentUserRankingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startAnimation(mainActivity.aniList[2])

        adapter = RankingAdapter(mainActivity, arrayListOf(),myName)
        binding.rankingRecycler.adapter = adapter

        //이번달 랭킹 갱신
        currentKeyDate = mainActivity.viewModel.todayKeyDate
        setRankingData(currentKeyDate)

        setUpListener()
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String) =
            UserRankingFragment().apply {
                arguments = Bundle().apply {
                    putString("myName", name)
                }
            }
    }

    private fun setUpListener(){

        // 내 랭킹으로 이동
        binding.goMyRanking.setOnClickListener {
            adapter?.myPosition.let {
                if (it!=null && it!=-1){binding.rankingRecycler.smoothScrollToPosition(it)}
            }
        }

        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@UserRankingFragment)
        }

        //다음 달로 이동
        binding.frontMonth.setOnClickListener {
            currentKeyDate = getKeyDate(+1)
            setRankingData(currentKeyDate)
        }

        //이전 달로 이동
        binding.backMonth.setOnClickListener {
            currentKeyDate = getKeyDate(-1)
            setRankingData(currentKeyDate)
        }
    }

    // 랭킹 데이터 갱신
    private fun setRankingData(keyDate : String){
        MonthDataManager(mainActivity.masterApp)
            .getRankingData(keyDate, paramFun ={ data,_->
                binding.keyDate.text = currentKeyDate
                if (data!=null && data.size>0){
                    setData()
                    val ranker = if (data.size>=3){data.subList(0,3)} else data
                    setTopRanker(ranker)
                    adapter?.setData(data)
                    binding.rankingRecycler.adapter = adapter
                }else{
                    setNotData()
                }
            })
    }

    private fun setNotData(){
        binding.dataBox.visibility = View.GONE
        binding.noDataBox.visibility = View.VISIBLE
    }

    private fun setData(){
        binding.noDataBox.visibility = View.GONE
        binding.dataBox.visibility = View.VISIBLE
    }

    //상단 랭커 갱신
    @SuppressLint("SetTextI18n")
    private fun setTopRanker(rankerList : MutableList<RecordData>){
        var i = 0
        for (ranker in rankerList){
            i+=1
            when (i) {
                1 -> {
                    binding.userName1.text = ranker.userId
                    binding.userPoint1.text = ranker.totalPoint.toString()+"P"
                }
                2 -> {
                    binding.userName2.text = ranker.userId
                    binding.userPoint2.text = ranker.totalPoint.toString()+"P"
                }
                3 -> {
                    binding.userName3.text = ranker.userId
                    binding.userPoint3.text = ranker.totalPoint.toString()+"P"
                }
            }
        }

    }

    // key data 변환
    private fun getKeyDate(plus : Int) : String{
        return if (currentKeyDate!=""){
            val current =  currentKeyDate.split(".")
            val month = current[1].toInt()+plus
            if (month>12){ //다음 년도
                "${current[0].toInt()+1}.1"
            }else if (month<1) { //이전 년도
                "${current[0].toInt() - 1}.12"
            }else{
                if (month<10){
                    "${current[0]}.0$month"
                }else {
                    "${current[0]}.$month"
                }
            }
        }else {
            currentKeyDate
        }
    }

}