package com.jinuemong.SwallowMonthJM.UIFragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.jinuemong.SwallowMonthJM.Adapter.MiniProfileHoAdapter
import com.jinuemong.SwallowMonthJM.DetailView.UserProfileFragment
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.Manager.UserManager
import com.jinuemong.SwallowMonthJM.databinding.FragmentSearchUserBinding

class SearchUserFragment : Fragment() {
    private var _binding : FragmentSearchUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var adapter: MiniProfileHoAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        onBackPressedCallback = object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                mainActivity.onFragmentGoBack(this@SearchUserFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchUserBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startAnimation(mainActivity.aniList[2])
        adapter = MiniProfileHoAdapter(mainActivity)
        binding.searchRecycler.adapter = adapter.apply {
            setOnItemClickListener(object :MiniProfileHoAdapter.OnItemClickListener{
                override fun itemClick(profileId: Int) {
                    mainActivity.onFragmentChange(UserProfileFragment.newInstance(profileId))
                }
            })
        }

        binding.searchView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                searchUser(p0.toString())
            }

        })

        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@SearchUserFragment)
        }

        binding.closeButton.setOnClickListener {
            adapter.setData(arrayListOf())
            binding.searchView.setText("")
        }
    }

    fun searchUser(param:String){
        if (param!="null" && param!="") {
            UserManager(mainActivity.masterApp, mainActivity)
                .searchUserProfiles(param, paramFun = { data, _ ->
                    if (data != null) {
                        adapter.setData(data)
                    } else {
                        adapter.setData(arrayListOf())
                    }
                })
        }else{
            adapter.setData(arrayListOf())
        }
    }

}