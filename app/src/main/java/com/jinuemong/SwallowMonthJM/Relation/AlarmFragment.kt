package com.jinuemong.SwallowMonthJM.Relation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.jinuemong.SwallowMonthJM.Adapter.AlarmListAdapter
import com.jinuemong.SwallowMonthJM.DetailView.UserProfileFragment
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Manager.AlarmManager
import com.jinuemong.SwallowMonthJM.databinding.FragmentAlarmBinding


class AlarmFragment : Fragment() {

    private var userName: String? = null
    private lateinit var mainActivity: MainActivity
    private var _binding : FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity

        onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@AlarmFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString("username")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startAnimation(mainActivity.aniList[2])
        userName?.let {
            AlarmManager(mainActivity.masterApp)
                .getMyAlarmList(it, paramFunc = { data,_->
                    if (data!=null){
                        binding.alarmList.adapter = AlarmListAdapter(mainActivity,data)
                            .apply {
                                setUpListener(object :AlarmListAdapter.OnItemClickListener{
                                    override fun itemClick(type: String, profileId : Int) {
                                        if(type=="FriendShip"){
                                            // 친구 요청한 프로필로 이동
                                            mainActivity.onFragmentChange(
                                                UserProfileFragment.newInstance(profileId))
                                        }
                                    }

                                })
                            }
                    }
                })
        }


        setUpListener()
    }

    private fun setUpListener(){
        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@AlarmFragment)
        }
    }
    companion object {

        @JvmStatic
        fun newInstance(username: String) =
            AlarmFragment().apply {
                arguments = Bundle().apply {
                    putString("username", username)
                }
            }
    }
}