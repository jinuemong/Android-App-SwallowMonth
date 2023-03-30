package com.example.SwallowMonthJM.DetailView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.SwallowMonthJM.Manager.UserManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.Adapter.MiniProfileAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.RelationManager
import com.example.SwallowMonthJM.Model.FriendData
import com.example.SwallowMonthJM.Model.Profile
import com.example.SwallowMonthJM.Server.MasterApplication
import com.example.SwallowMonthJM.Relation.MessageRoomFragment
import com.example.SwallowMonthJM.Relation.MyFriendFragment
import com.example.SwallowMonthJM.Relation.TotalFriendFragment
import com.example.SwallowMonthJM.Unit.MessageBox
import com.example.SwallowMonthJM.databinding.FragmentUserProfileBinding


class UserProfileFragment() : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var relationManager: RelationManager
    private lateinit var callback: OnBackPressedCallback
    private var _binding : FragmentUserProfileBinding?=null
    private val binding get() = _binding!!
    private var profileId : Int=-1
    private var profileName = ""
    private lateinit var myProfile :Profile
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity=  context as MainActivity
        myProfile= mainActivity.viewModel.myProfile
        callback = object  : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@UserProfileFragment)
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
        relationManager = RelationManager(mainActivity.masterApp)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments!=null){
            profileId = requireArguments().getInt("profileId",-1)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isMainView()
        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@UserProfileFragment)
        }

        //프로필 갱신
        if (profileId!=-1){
            UserManager(mainActivity.masterApp,mainActivity)
                .getUserProfile(profileId, paramFun = { data, _->
                    if (data!=null){
                        binding.userName.text = data.userName
                        binding.profileName.text = "${data.userName} Profile"
                        if (data.userComment!="") {
                            binding.userComment.text = data.userComment
                        }
                        Glide.with(mainActivity)
                            .load(data.userImage)
                            .into(binding.userImage)

                        profileName = data.userName

                        setFriendList()
                        setUpListener()
                        setFriendShip()
                    }
                })
        }

    }

    companion object{
        @JvmStatic
        fun newInstance(profileId: Int) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt("profileId",profileId)
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun setFriendList(){
        // max 10
        relationManager.getFriendList(profileName, paramFunc ={ data,_->
                if (data!=null){
                    binding.totalFriend.text = "total ${data.size}"
                    //데이터 수 지정
                    if (data.size>=10) data.subList(0,10)
                    val adapter = MiniProfileAdapter(mainActivity,data)
                    binding.friendList.adapter = adapter.apply {
                        setOnItemClickListener(object : MiniProfileAdapter.OnItemClickListener{
                            override fun onItemClick(item: Profile) {
                                //클릭 프로필로 이동
                                mainActivity.onFragmentChange(newInstance(item.profileId))
                            }
                        })
                    }
                }else{
                    binding.totalFriend.text = "0"
                    binding.notFriends.visibility = View.VISIBLE
                    binding.moreFriend.visibility = View.INVISIBLE
                }
            })
    }

    private fun setUpListener(){
        binding.moreFriend.setOnClickListener {
            if(profileName==myProfile.userName){
                mainActivity.onFragmentChange(MyFriendFragment())
            }else {
                mainActivity.onFragmentChange(TotalFriendFragment.newInstance(profileName))
            }
        }
    }

    //relation에 따른 뷰 설정
    private fun setFriendShip(){
        // my view
        if (myProfile.profileId == profileId) {
            isMyView()
        } else {
            //친구 상태 확인
            relationManager
                .checkFriend(myProfile.userName,profileId, paramFunc = { checkFriend,err->
                    if (checkFriend!=null){
                        Log.d("checkFriendData",checkFriend.type.toString()+","+checkFriend.frId)
                        when(checkFriend.type){
                            1->{ // 친구
                                isFriend(checkFriend.frId)
                            }2->{ //요청 보냄
                                isAddFriendStatus(checkFriend.frId)
                            }3->{ //요청 받음
                                isGetFriendStatus(checkFriend.frId)
                            }4->{ //아무런 관계 없음
                                isMainView()
                            }else->{}
                        }
                    }else{
                        Log.d("checkFriendData",err.toString())
                    }
                })
        }
    }

    // 내 뷰 상태
    private fun isMyView(){
        binding.addFriend.visibility = View.GONE
        binding.sendMessage.visibility = View.GONE
        binding.sendData.visibility = View.GONE
        binding.isFriend.visibility = View.GONE
    }

    //친구 요청 받은 상태 type 3
    @SuppressLint("SetTextI18n")
    private fun isGetFriendStatus(frId: Int){
        binding.addFriend.visibility = View.GONE
        binding.sendMessage.visibility = View.GONE
        binding.sendData.visibility = View.VISIBLE
        binding.isFriend.visibility = View.GONE
        //친구 수락
        binding.sendData.apply {
            text = "Accept friend"
            setOnClickListener {
                acceptFriend(frId)
            }
        }
    }

    //친구 추가 보낸 상태 type 2
    private fun isAddFriendStatus(frId:Int){
        binding.addFriend.visibility = View.GONE
        binding.sendMessage.visibility = View.GONE
        binding.sendData.visibility = View.VISIBLE
        binding.isFriend.visibility = View.GONE

        //친구 취소
        binding.sendData.setOnClickListener {
            delFriendShip(frId)
        }
    }

    //기본 상태 type 4
    private fun isMainView() {
        binding.sendMessage.visibility = View.VISIBLE
        binding.addFriend.visibility = View.VISIBLE
        binding.sendData.visibility = View.GONE
        binding.isFriend.visibility = View.GONE
        // 메시지 기능
        binding.sendMessage.setOnClickListener {
            val messageBox = MessageBox.newInstance("Only friend can send message.")
            messageBox.show(mainActivity.frManger,null)
            messageBox.apply {
                setOnclickListener(object :MessageBox.OnItemClickListener{
                    override fun onItemClick() {
                        messageBox.dismiss()
                    }
                })
            }
        }
        //친구 추가 기능 -> 친구 추가 상태로 바뀜
        binding.addFriend.setOnClickListener {
            RelationManager(mainActivity.masterApp)
                .makeNewFriendRelation(myProfile.userName,profileId,profileName
                    , paramFunc = { data,_->
                        if (data!=null) {
                            isAddFriendStatus(data.frId)
                        }else{

                        }
                    })
        }
    }

    //친구 상태 type 1
    private fun isFriend(frId: Int){
        binding.sendMessage.visibility = View.VISIBLE
        binding.addFriend.visibility = View.GONE
        binding.sendData.visibility = View.GONE
        binding.isFriend.visibility = View.VISIBLE

        //친구 취소
        binding.isFriend.setOnClickListener {
            val messageBox = MessageBox.newInstance("All relationships " +
                    "including messages, " +
                    "are deleted to the user where they are deleted?")
            messageBox.show(mainActivity.frManger,null)
            messageBox.apply {
                setOnclickListener(object :MessageBox.OnItemClickListener{
                    override fun onItemClick() {
                        messageBox.dismiss()
                        delFriendShip(frId)
                    }
                })
            }
        }

        //메시지
        binding.sendMessage.setOnClickListener {
            mainActivity.onFragmentChange(
                MessageRoomFragment.newInstance(frId)
            )
        }
    }

    private fun delFriendShip(frId :Int){
        relationManager.delFriendShip(frId,
            paramFunc = { _,message->
                if (message==null){
                    isMainView()
                }
            })
    }
    private fun acceptFriend(frId: Int){
        relationManager.addFUser(frId,myProfile.userName,profileId
            , paramFunc = { _,message->
            if (message==null){
                isFriend(frId)
            }
        })
    }
}