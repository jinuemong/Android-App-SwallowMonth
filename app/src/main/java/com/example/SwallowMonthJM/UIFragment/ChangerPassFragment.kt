package com.example.SwallowMonthJM.UIFragment

import android.content.Context
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentChangerPassBinding
import com.example.SwallowMonthJM.Manager.UserManager

class ChangerPassFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    private var _binding : FragmentChangerPassBinding?= null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangerPassBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            mainActivity.onFragmentGoBack(this@ChangerPassFragment)
        }

        binding.changeButton.setOnClickListener {

            if(getUserPass1()==""){
                Toast.makeText(mainActivity,
                    "Enter password", Toast.LENGTH_SHORT)
                    .show()
            }else if(getUserPass2()==""){
                Toast.makeText(mainActivity,
                    "Enter your password one more", Toast.LENGTH_SHORT)
                    .show()
            }else if(getUserPass1()!=getUserPass2()){
                Toast.makeText(mainActivity,
                    "Password is wrong", Toast.LENGTH_SHORT)
                    .show()
            }else{
                changePass()
            }
        }
    }


    private fun getUserPass1(): String {
        return binding.pw1.text.toString()
    }

    private fun getUserPass2(): String {
        return binding.pw2.text.toString()
    }

    private fun getUserName() : String{
        return mainActivity.viewModel.myProfile.userName
    }


    private fun changePass(){
        UserManager(mainActivity.masterApp,mainActivity)
            .updateUserPassword(getUserName(),getUserPass1(), paramFun = { _, message ->
                if (message!=null){
                    Toast.makeText(mainActivity,message,Toast.LENGTH_LONG).show()
                }
                mainActivity.onFragmentGoBack(this@ChangerPassFragment)
            })
    }
}