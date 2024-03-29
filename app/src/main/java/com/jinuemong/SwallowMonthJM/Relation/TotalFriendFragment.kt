package com.jinuemong.SwallowMonthJM.Relation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jinuemong.SwallowMonthJM.Adapter.MiniProfileAdapter
import com.jinuemong.SwallowMonthJM.DetailView.UserProfileFragment
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Manager.RelationManager
import com.jinuemong.SwallowMonthJM.Model.Profile
import com.jinuemong.SwallowMonthJM.databinding.FragmentTotalFriendBinding

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startAnimation(mainActivity.aniList[2])

        binding.text.text = "$username Friend List"

        username?.let {
            RelationManager(mainActivity.masterApp)
                .getFriendListR(it,-1, paramFunc = { data, message->
                    if (message==null){
                        val adapterData = if (data==null || data.count==0) arrayListOf() else data.friends
                        val adapter = MiniProfileAdapter(mainActivity,adapterData)
                        binding.friendList.adapter = adapter.apply {
                            setOnItemClickListener(object : MiniProfileAdapter.OnItemClickListener{
                                override fun onItemClick(item: Profile) {
                                    mainActivity.onFragmentChange(UserProfileFragment.newInstance(item.profileId))
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
            TotalFriendFragment().apply {
                arguments = Bundle().apply {
                    putString("userName", userName)
                }
            }
    }
}
