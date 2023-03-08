package com.example.SwallowMonthJM.Relation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.SwallowMonthJM.Adapter.MessageRoomAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.MessageManager
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentMessageListBinding


class MessageListFragment : Fragment() {
    private var profileId = -1
    private var _binding : FragmentMessageListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profileId = it.getInt("profileId")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MessageManager(mainActivity.masterApp)
            .getMessageRoomList(mainActivity.viewModel.myProfile.userName, paramFunc = { data,message->
                if (data!=null){
                    val adapter = MessageRoomAdapter(mainActivity,data)
                    binding.messageList.adapter = adapter.apply {
                        setOnItemClickListener(object : MessageRoomAdapter.OnItemClickListener{
                            override fun onItemClick(frId:Int) {
                                mainActivity.onFragmentChange(MessageRoomFragment.newInstance(frId))
                            }
                        })
                    }
                }else{
                    Log.d("error : messageListFragment",message.toString())
                }
            })
    }
    companion object {

        @JvmStatic
        fun newInstance(profileId : Int) =
            MessageListFragment().apply {
                arguments = Bundle().apply {
                    putInt("profileId", profileId)
                }
            }
    }
}