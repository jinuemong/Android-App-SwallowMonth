package com.jinuemong.SwallowMonthJM.MainFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.jinuemong.SwallowMonthJM.Adapter.MiniProfileAdapter
import com.jinuemong.SwallowMonthJM.Adapter.RecordAdapter
import com.jinuemong.SwallowMonthJM.DetailView.UserProfileFragment
import com.jinuemong.SwallowMonthJM.LoginActivity
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Manager.MonthDataManager
import com.jinuemong.SwallowMonthJM.Manager.RelationManager
import com.jinuemong.SwallowMonthJM.Model.Profile
import com.jinuemong.SwallowMonthJM.Model.RecordData
import com.jinuemong.SwallowMonthJM.Relation.MessageListFragment
import com.jinuemong.SwallowMonthJM.Relation.MyFriendFragment
import com.jinuemong.SwallowMonthJM.UIFragment.*
import com.jinuemong.SwallowMonthJM.Unit.getPhotoUrl
import com.jinuemong.SwallowMonthJM.databinding.FragmentUserUIBinding



class FragmentUserUI : Fragment() {
    private var _binding : FragmentUserUIBinding?=null
    private val binding get() = _binding!!
    private lateinit var monthDataManager: MonthDataManager

    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        monthDataManager = MonthDataManager(mainActivity.masterApp)

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
        mainActivity.viewModel.eventSetProfile.observe(mainActivity, Observer {
            initView()
        })

        //추천 유저
    }

    private fun initView(){

        mainActivity.viewModel.myProfile.apply {
            Glide.with(mainActivity)
                .load(getPhotoUrl(userImage,mainActivity.masterApp.baseUrl))
                .into(binding.userImage)
            binding.userName.text = this.userName
            if (this.userComment!="") {
                binding.userComment.text = this.userComment
            }

            setRecordList(this.userName)

            // 추천 유저
            RelationManager(mainActivity.masterApp)
                .getRandomProfileList(this.profileId, paramFunc = {data,_->
                    if (data!=null && data.size>0){
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

        binding.userRanking.setOnClickListener {
            mainActivity.onFragmentChange(UserRankingFragment.newInstance(
                mainActivity.viewModel.myProfile.userName
            ))
        }

        binding.myAchievement.setOnClickListener {
            mainActivity.onFragmentChange(RecordFragment.newInstance(mainActivity.userName))
        }

        binding.moreRecord.setOnClickListener {
            mainActivity.onFragmentChange(RecordFragment.newInstance(mainActivity.userName))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setRecordList(profileName : String){
        monthDataManager.getUserRecordData(profileName, paramFun = {data,_->
            if (data!=null && data.size>0){
                val dataSet = if (data.size>5) data.subList(0,5) else data
                val adapter = RecordAdapter(mainActivity, dataSet as ArrayList<RecordData>)
                binding.recordList.adapter = adapter
            }else{
                binding.notRecords.visibility = View.VISIBLE
                binding.moreRecord.visibility = View.INVISIBLE
            }
        })
    }

}

