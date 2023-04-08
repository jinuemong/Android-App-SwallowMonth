package com.example.SwallowMonthJM.MainFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.Adapter.MiniProfileAdapter
import com.example.SwallowMonthJM.DetailView.UserProfileFragment
import com.example.SwallowMonthJM.LoginActivity
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.RelationManager
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Relation.MessageListFragment
import com.example.SwallowMonthJM.Relation.MyFriendFragment
import com.example.SwallowMonthJM.UIFragment.ChangerPassFragment
import com.example.SwallowMonthJM.UIFragment.ProfileUpdateFragment
import com.example.SwallowMonthJM.UIFragment.SearchUserFragment
import com.example.SwallowMonthJM.Unit.getPhotoUrl
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

        //추천 유저
    }

    private fun initView(){
        Log.d("initViewMain4","")

        mainActivity.viewModel.myProfile.apply {
            Glide.with(mainActivity)
                .load(getPhotoUrl(userImage,mainActivity.masterApp.baseUrl))
                .into(binding.userImage)
            binding.userName.text = this.userName
            if (this.userComment!="") {
                binding.userComment.text = this.userComment
            }

            // 추천 유저
            RelationManager(mainActivity.masterApp)
                .getRandomProfileList(this.profileId, paramFunc = {data,_->
                    if (data!=null){
                        val adapter = MiniProfileAdapter(mainActivity,data,)
                        binding.recommendUser.adapter =adapter.apply {
                            setOnItemClickListener(object : MiniProfileAdapter.OnItemClickListener{
                                override fun onItemClick(item: Profile) {
                                    mainActivity.onFragmentChange(UserProfileFragment.newInstance(item.profileId))
                                }
                            })
                        }
                    }else{
                        binding.notRecommend.visibility = View.VISIBLE
                    }
                })
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

        binding.myFriend.setOnClickListener {
            mainActivity.onFragmentChange(MyFriendFragment())
        }

        binding.message.setOnClickListener {
            mainActivity.onFragmentChange(MessageListFragment.newInstance(
                mainActivity.viewModel.myProfile.profileId
            ))
        }

        binding.findPeople.setOnClickListener {
            mainActivity.onFragmentChange(SearchUserFragment())
        }

        binding.changePass.setOnClickListener {
            mainActivity.onFragmentChange(ChangerPassFragment())
        }

    }

}

