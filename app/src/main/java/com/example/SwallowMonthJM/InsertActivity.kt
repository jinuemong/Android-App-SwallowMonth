package com.example.SwallowMonthJM

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.SwallowMonthJM.Model.GetUser
import com.example.SwallowMonthJM.Server.MasterApplication
import com.example.SwallowMonthJM.Unit.errorConvert
import com.example.SwallowMonthJM.databinding.ActivityInsertBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInsertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backButton.setOnClickListener {
            val intent = Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.insertButton.setOnClickListener {
            if (getUserName()==""){
                Toast.makeText(this@InsertActivity,
                    "Enter Id", Toast.LENGTH_SHORT)
                    .show()
            }else if(getUserPass1()==""){
                Toast.makeText(this@InsertActivity,
                    "Enter password", Toast.LENGTH_SHORT)
                    .show()
            }else if(getUserPass2()==""){
                Toast.makeText(this@InsertActivity,
                    "Enter your password one more", Toast.LENGTH_SHORT)
                    .show()
            }else if(getUserPass1()!=getUserPass2()){
                Toast.makeText(this@InsertActivity,
                    "Password is wrong", Toast.LENGTH_SHORT)
                    .show()
            }else{
                register()
            }
        }
    }

    private fun register(){
        val masterApp = (application as MasterApplication)
        masterApp.createRetrofit(this@InsertActivity)
        masterApp.service.registerUser(
            getUserName(),getUserPass1()
        ).enqueue(object : Callback<GetUser>{
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                if (response.isSuccessful){
                    //회원가입 성공
                    //다음 화면으로 넘어가기
                    val intent = Intent(applicationContext,LoginActivity::class.java)
                    startActivity(intent)
                }else{
                    val err = errorConvert(response.errorBody())
                    Toast.makeText(this@InsertActivity,err
                        ,Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetUser>, t: Throwable) {
                Toast.makeText(this@InsertActivity,"Network error",Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }
    private fun getUserName(): String {
        return binding.insertId.text.toString()
    }

    private fun getUserPass1(): String {
        return binding.insertPw1.text.toString()
    }

    private fun getUserPass2(): String {
        return binding.insertPw2.text.toString()
    }
}