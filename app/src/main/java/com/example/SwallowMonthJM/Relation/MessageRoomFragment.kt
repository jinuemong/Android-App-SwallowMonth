package com.example.SwallowMonthJM.Relation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentMessageRoomBinding


class MessageRoomFragment : Fragment() {
    private var frId : Int = -1
    private var _binding : FragmentMessageRoomBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            frId = it.getInt("frId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageRoomBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(frId : Int) =
            MessageRoomFragment().apply {
                arguments = Bundle().apply {
                    putInt("frId",frId)
                }
            }
    }
}