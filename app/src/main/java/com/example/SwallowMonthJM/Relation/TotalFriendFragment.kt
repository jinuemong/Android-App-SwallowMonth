package com.example.SwallowMonthJM.Relation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.SwallowMonthJM.Adapter.MiniProfileAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.RelationManager
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.databinding.FragmentTotalFriendBinding

// other 유저 친구 목록
class TotalFriendFragment() : Fragment() {
    private var username : String? = null
    private var _binding : FragmentTotalFriendBinding?= null
    private val binding get() = _binding!!


    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments!=null){
            username = arguments?.getString("userName",null)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentTotalFriendBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username?.let {
            RelationManager(mainActivity.application as MasterApplication)
                .getFriendList(it, paramFunc = { data, message->
                    if (message==null){
                        val adapterData = if (data==null || data.size==0) arrayListOf() else data
                        val adapter = MiniProfileAdapter(mainActivity,adapterData)
                        binding.friendList.adapter = adapter.apply {
                            setOnItemClickListener(object : MiniProfileAdapter.OnItemClickListener{
                                override fun onItemClick(item: Profile) {

                                }
                            })
                        }
                    }else{
                        Toast.makeText(mainActivity,message,Toast.LENGTH_SHORT).show()
                    }

                    //클릭 이벤트 적용
                    setUpListener()
                })
        }

    }

    private fun setUpListener(){
        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@TotalFriendFragment)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(userName : String) =
            MyFriendFragment().apply {
                arguments = Bundle().apply {
                    putString("userName", userName)
                }
            }
    }
}
