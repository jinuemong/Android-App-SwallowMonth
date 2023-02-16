package com.example.SwallowMonthJM.UIFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentProfileUpdateBinding

class ProfileUpdateFragment : Fragment(){
    private var _binding : FragmentProfileUpdateBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileUpdateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListener()
    }

    private fun setUpListener(){
        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@ProfileUpdateFragment)
        }

        //제출
        binding.commitButton.setOnClickListener {

        }
    }
}