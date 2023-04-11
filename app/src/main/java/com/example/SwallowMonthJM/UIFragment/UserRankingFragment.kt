package com.example.SwallowMonthJM.UIFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.SwallowMonthJM.Adapter.RankingAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.MonthDataManager
import com.example.SwallowMonthJM.Model.RecordData
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentUserRankingBinding
import org.mozilla.javascript.tools.jsc.Main

class UserRankingFragment : Fragment() {
    private var myName = ""
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
    }

    private fun setRankingData(keyDate : String){
        MonthDataManager(mainActivity.masterApp)
            .getRankingData(keyDate, paramFun ={ data,message->
                if (data!=null){
                    binding.noDataBox.visibility = View.GONE
                    binding.dataBox.visibility = View.VISIBLE
                    val ranker = if (data.size>=3){data.subList(0,3)} else data
                    setTopRanker(ranker)
                    adapter = 
                }else{
                    binding.dataBox.visibility = View.GONE
                    binding.noDataBox.visibility = View.VISIBLE
                }
            })
    }

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

    private fun getKeyDate(plus : Int){

    }

}