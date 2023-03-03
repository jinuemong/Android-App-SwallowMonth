package com.example.SwallowMonthJM.Relation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.RelationManager
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentMyFriendBinding

class MyFriendFragment : Fragment() {
    private var _binding : FragmentMyFriendBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

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
        _binding = FragmentMyFriendBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RelationManager(mainActivity.application as MasterApplication)
            .getFriendList(mainActivity.viewModel.myProfile.userName, paramFunc = { data,message->
                
            })
    }


    private fun setUpListener(){
        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@MyFriendFragment)
        }
    }



}