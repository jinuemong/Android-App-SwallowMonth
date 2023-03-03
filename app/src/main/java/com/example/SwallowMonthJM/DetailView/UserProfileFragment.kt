package com.example.SwallowMonthJM.DetailView

import android.content.Context
import android.os.Bundle
import com.example.SwallowMonthJM.Manager.UserManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentUserProfileBinding
import org.mozilla.javascript.tools.jsc.Main


class UserProfileFragment() : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var callback: OnBackPressedCallback
    private var _binding : FragmentUserProfileBinding?=null
    private val binding get() = _binding!!
    private var profileId : Int=-1
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity=  context as MainActivity
        callback = object  : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@UserProfileFragment)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments!=null){
            profileId = requireArguments().getInt("profileId",-1)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (profileId!=-1){
            UserManager(mainActivity.application as MasterApplication,mainActivity)
                .getUserProfile(profileId, paramFun = { data, _->
                    if (data!=null){
                        binding.userName.text = data.userName
                        binding.userComment.text = data.userComment
                        Glide.with(mainActivity)
                            .load(data.userImage)
                            .into(binding.userImage)
                    }
                })

        }
    }

    companion object{
        @JvmStatic
        fun newInstance(profileId: Int) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt("profileId",profileId)
                }
            }
    }

}