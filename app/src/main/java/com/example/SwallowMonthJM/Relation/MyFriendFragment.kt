package com.example.SwallowMonthJM.Relation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.SwallowMonthJM.Adapter.MiniProfileAdapter
import com.example.SwallowMonthJM.DetailView.UserProfileFragment
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.RelationManager
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.databinding.FragmentMyFriendBinding

class MyFriendFragment : Fragment() {
    private var _binding : FragmentMyFriendBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyFriendBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startAnimation(mainActivity.aniList[2])

        RelationManager(mainActivity.masterApp)
            .getFriendListR(mainActivity.viewModel.myProfile.userName,-1, paramFunc = { data, message->
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
                    Toast.makeText(mainActivity,message, Toast.LENGTH_SHORT).show()
                }
                setUpListener()
            })
    }


    private fun setUpListener(){
        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@MyFriendFragment)
        }
    }



}