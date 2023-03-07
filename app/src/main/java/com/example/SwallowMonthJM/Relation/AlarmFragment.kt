package com.example.SwallowMonthJM.Relation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.AlarmManager
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentAlarmBinding


class AlarmFragment : Fragment() {

    private var userName: String? = null
    private lateinit var mainActivity: MainActivity
    private var _binding : FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString("username")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName?.let {
            AlarmManager(mainActivity.masterApp)
                .getMyAlarmList(it, paramFunc = { data,_->
                    if (data!=null){

                    }
                })
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(username: String) =
            AlarmFragment().apply {
                arguments = Bundle().apply {
                    putString("username", username)
                }
            }
    }
}