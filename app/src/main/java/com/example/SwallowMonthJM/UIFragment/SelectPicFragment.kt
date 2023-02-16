package com.example.SwallowMonthJM.UIFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentSelectPicBinding

class SelectPicFragment : Fragment() {
    private var _binding : FragmentSelectPicBinding? = null
    private val binding  get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectPicBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}