package com.jinuemong.SwallowMonthJM.Relation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jinuemong.SwallowMonthJM.Adapter.MessageRoomAdapter
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Manager.MessageManager
import com.jinuemong.SwallowMonthJM.databinding.FragmentMessageListBinding


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
        view.startAnimation(mainActivity.aniList[2])
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