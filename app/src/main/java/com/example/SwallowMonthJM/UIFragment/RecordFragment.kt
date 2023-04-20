package com.example.SwallowMonthJM.UIFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.SwallowMonthJM.Adapter.RecordAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.MonthDataManager
import com.example.SwallowMonthJM.databinding.FragmentRecordBinding

class RecordFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding : FragmentRecordBinding? = null
    private val binding get() = _binding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var userName = ""
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@RecordFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString("name").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentRecordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startAnimation(mainActivity.aniList[2])

        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@RecordFragment)
        }

        MonthDataManager(mainActivity.masterApp)
            .getUserRecordData(userName, paramFun = {data,_->
                if (data!=null && data.size>0){
                    binding.recordList.adapter = RecordAdapter(mainActivity,data)
                    binding.notRecordsMent.visibility = View.GONE
                    binding.recordList.visibility = View.VISIBLE
                }else{
                    binding.recordList.visibility = View.GONE
                    binding.notRecordsMent.visibility = View.VISIBLE
                }
            })
    }

    companion object{
        fun newInstance(name: String) =
            RecordFragment().apply {
                arguments = Bundle().apply {
                    putString("name",name)
                }
            }
    }
}