package com.example.SwallowMonthJM.DetailView

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentUserProfileBinding
import org.mozilla.javascript.tools.jsc.Main


class UserProfileFragment() : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var callback: OnBackPressedCallback
    private var _binding : FragmentUserProfileBinding?=null
    private val binding get() = _binding!!
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity=  context as MainActivity
        callback = object  : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@UserProfileFragment)
            }

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater,container,false)
        return binding.root
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