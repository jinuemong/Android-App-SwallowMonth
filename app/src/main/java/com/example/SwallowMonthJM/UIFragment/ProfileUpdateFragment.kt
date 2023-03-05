package com.example.SwallowMonthJM.UIFragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentProfileUpdateBinding
import com.example.SwallowMonthJM.Manager.UserManager
import com.example.SwallowMonthJM.Model.Profile
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class ProfileUpdateFragment : Fragment() {
    private var _binding: FragmentProfileUpdateBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var userManager: UserManager
    private var updateProfile : Profile? = null
    private var imageUri : Uri?=null
    private var selectPicFragment : SelectPicFragment? = null
    private lateinit var callback : OnBackPressedCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        userManager = UserManager(mainActivity.application as MasterApplication, mainActivity)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@ProfileUpdateFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.viewModel.myProfile.apply {
            updateProfile = this
            Glide.with(mainActivity)
                .load(userImage)
                .into(binding.userImage)

            binding.insertId.setText(userName)
            binding.insertComment.setText(userComment)

        }

        // 슬라이드로 이미지 변경 프래그먼트 처리
        // 클릭 이벤트 시 이미지 가져옴
        selectPicFragment=  SelectPicFragment(binding.slideFrameInUpdateProfile)
            .apply {
            setOnItemClickListener(object :SelectPicFragment.OnItemClickListener{
                override fun onItemClick(lastUri: Uri?) {
                    imageUri =
                        if (lastUri.toString()!=""){
                        lastUri
                    }else{
                        Uri.parse(mainActivity.viewModel.myProfile.userImage)
                    }
                    Glide.with(mainActivity)
                        .load(imageUri)
                        .into(binding.userImage)
                }
            })
        }
        //슬라이드 레이아웃 view 설정
        selectPicFragment?.let {fragment->
            mainActivity.frManger.beginTransaction()
                .replace(R.id.slide_layout_in_update_profile,fragment)
                .commit()
        }
        setUpListener()
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@ProfileUpdateFragment)
        }

        //키 리스너
        binding.insertComment.setOnKeyListener{ _,keyCode, event ->
            var handled = false

            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                addTypeUserComment()
                handled = true
            }
            handled
        }
        binding.insertId.setOnKeyListener{ _,keyCode, event->
            var handled = false

            if (event.action ==KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                handled = true
                addTypeUserName()
            }
            handled
        }
        //이미지 변경
        binding.userImage.setOnClickListener {
            val state = binding.slideFrameInUpdateProfile.panelState
            // 닫힌 상태일 경우 열기
            if (state == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                binding.slideFrameInUpdateProfile.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
            }
            // 열린 상태일 경우 닫기
            else if (state == SlidingUpPanelLayout.PanelState.EXPANDED) {
                binding.slideFrameInUpdateProfile.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }
        }
        //제출
        binding.commitButton.setOnClickListener {
            updateProfile?.let { up ->

                if(binding.insertComment.text!=null){addTypeUserComment()}
                if(binding.insertId.text!=null){addTypeUserName()}

                userManager.setUserProfile(up,imageUri, paramFun = { newProfile,erMessage->
                    if (newProfile != null && erMessage=="") {
                        UserManager((mainActivity.application as MasterApplication),mainActivity)
                            .getUserProfile(newProfile.profileId, paramFun = { profile,message->
                                if (profile!=null) {
                                    mainActivity.viewModel.setProfile(profile)
                                    mainActivity.setProfile(profile)
                                    mainActivity.onFragmentGoBack(this@ProfileUpdateFragment)
                                }else{
                                    Toast.makeText(mainActivity,message,Toast.LENGTH_SHORT).show()
                                }
                            })

                    }else{
                        //에러 메시지 띄우기
                        Toast.makeText(mainActivity.applicationContext,erMessage
                            ,Toast.LENGTH_SHORT).show()
                    }

                })

            }
        }
    }

    private fun addTypeUserComment(){
        binding.insertComment.apply {
            if (text != null) {
                updateProfile?.userComment = text.toString()
            }
        }
    }

    private fun addTypeUserName(){
        binding.insertId.apply {
            if (text!=null){
                updateProfile?.userName = text.toString()
            }
        }
    }
}