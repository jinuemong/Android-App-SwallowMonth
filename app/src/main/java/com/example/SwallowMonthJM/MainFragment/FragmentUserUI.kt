package com.example.SwallowMonthJM.MainFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.LoginActivity
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.UIFragment.ProfileUpdateFragment
import com.example.SwallowMonthJM.databinding.FragmentUserUIBinding



class FragmentUserUI : Fragment() {
    private var _binding : FragmentUserUIBinding?=null
    private val binding get() = _binding!!

    lateinit var mainActivity: MainActivity
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
        _binding = FragmentUserUIBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpListener()

        //옵저버를 통한 프로필 업데이트
        mainActivity.viewModel.eventSetData.observe(mainActivity, Observer {
            initView()
        })
    }

    private fun initView(){
        mainActivity.viewModel.profile.apply {
            Glide.with(mainActivity)
                .load(this.userImage)
                .into(binding.userImage)
            binding.userName.text = this.userName
        }

    }

    private fun setUpListener(){
        binding.logout.setOnClickListener {
            val intent = Intent(mainActivity, LoginActivity::class.java)
            intent.putExtra("logout",true)
            startActivity(intent)
        }

        binding.edit.setOnClickListener {
            mainActivity.onFragmentChange(ProfileUpdateFragment())
        }
    }
}

