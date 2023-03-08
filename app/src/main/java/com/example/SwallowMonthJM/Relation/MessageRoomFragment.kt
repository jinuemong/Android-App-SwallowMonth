package com.example.SwallowMonthJM.Relation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.MessageManager
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentMessageRoomBinding


class MessageRoomFragment : Fragment() {
    private var frId : Int = -1
    private var _binding : FragmentMessageRoomBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (frId!=-1) {
            MessageManager(mainActivity.masterApp)
                .getMessageList(frId, paramFunc = {data,message->
                    if (data!=null){
                        //adapter (set data)
                    }else{
                        Log.d("Message Room Fragment Error : ",message.toString())
                    }
                })
        }
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