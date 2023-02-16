package com.example.SwallowMonthJM.UIFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentProfileUpdateBinding
import com.example.SwallowMonthJM.Manager.UserManager
import com.example.SwallowMonthJM.Model.Profile

class ProfileUpdateFragment : Fragment() {
    private var _binding: FragmentProfileUpdateBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var userManager: UserManager
    private var updateProfile : Profile? = null
    private var imageUri = ""
    private val selectPicFragment=  SelectPicFragment()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        userManager = UserManager(mainActivity.application as MasterApplication, mainActivity)
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
        mainActivity.viewModel.profile.apply {
            updateProfile = this
            Glide.with(mainActivity)
                .load(userImage)
                .into(binding.userImage)

            binding.insertId.setText(userName)
            binding.insertComment.setText(userComment)
        }
        setUpListener()
        selectPicFragment.apply {
            setOnItemClickListener(object :SelectPicFragment.OnItemClickListener{
                override fun onItemClick(lastUri: String) {
                    if (imageUri!=""){
                        Glide.with(mainActivity)
                            .load(lastUri)
                            .into(binding.userImage)
                        imageUri = lastUri
                    }else{
                        //사진이 없다면 원래 이미지 등록
                        Glide.with(mainActivity)
                            .load(mainActivity.viewModel.profile.userImage)
                            .into(binding.userImage)
                    }
                }
            })
        }
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@ProfileUpdateFragment)
        }

        //이미지 변경
        binding.userImage.setOnClickListener {

            mainActivity.onFragmentChange(SelectPicFragment())
        }
        //제출
        binding.commitButton.setOnClickListener {
            updateProfile?.let { up ->
                userManager.setUserProfile(up,imageUri, paramFun = { newProfile,erMessage->
                    if (newProfile != null && erMessage=="") {
                        mainActivity.viewModel.profile = newProfile
                    }else{
                        //에러 메시지 띄우기
                        Toast.makeText(mainActivity.applicationContext,erMessage
                            ,Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}