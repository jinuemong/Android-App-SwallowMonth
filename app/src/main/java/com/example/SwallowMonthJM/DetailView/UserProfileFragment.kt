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
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.Relation.MessageRoomFragment
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
        relationManager = RelationManager(mainActivity.application as MasterApplication)
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
            UserManager(mainActivity.application as MasterApplication,mainActivity)
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
            mainActivity.onFragmentChange(TotalFriendFragment.newInstance(profileName))
        }
    }

    //relation에 따른 뷰 설정
    private fun setFriendShip(){
        // my view
        if (myProfile.profileId == profileId) {
            isMyView()
        } else {
            //친구 상태 확인 1: not , 2 : post add Friend ,3: friend
            relationManager
                .checkFriend(myProfile.userName,profileId, paramFunc = { friendData,message->
                    if (message==null){
                        if (friendData?.friendData==null){ //no Friend
                            isMainView()
                        }else{
                            if(friendData.friendData.fUserPost.size==1){ //친구 요청만 감
                                isAddFriendStatus(friendData.friendData.frId)

                            }else if (friendData.friendData.fUserPost.size==2){ //친구 상태
                                isFriend(friendData)
                            }
                        }
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

    //친구 추가 보낸 상태
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

    //기본 상태
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
            RelationManager(mainActivity.application as MasterApplication)
                .makeNewFriendRelation(myProfile.userName,profileId,profileName
                    , paramFunc = { data,message->
                        if (data!=null) {
                            Log.d("sdlfjsdofjsdof",data.toString())
                            isAddFriendStatus(data.frId)
                        }else{
                            Log.d("sdlfjsdofjsdof",message.toString())

                        }
                    })
        }
    }

    //친구 상태
    private fun isFriend(friendData:FriendData){
        binding.sendMessage.visibility = View.VISIBLE
        binding.addFriend.visibility = View.GONE
        binding.sendData.visibility = View.GONE
        binding.isFriend.visibility = View.VISIBLE

        //친구 취소
        binding.sendData.setOnClickListener {
            val messageBox = MessageBox.newInstance("All relationships " +
                    "including messages, " +
                    "are deleted to the user where they are deleted?")
            messageBox.show(mainActivity.frManger,null)
            messageBox.apply {
                setOnclickListener(object :MessageBox.OnItemClickListener{
                    override fun onItemClick() {
                        messageBox.dismiss()
                        delFriendShip(friendData.friendData.frId)
                    }
                })
            }
        }

        //메시지
        binding.sendMessage.setOnClickListener {
            mainActivity.onFragmentChange(
                MessageRoomFragment.newInstance(friendData.friendData.frId)
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
}