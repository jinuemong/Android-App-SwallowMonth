package com.example.SwallowMonthJM

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.SwallowMonthJM.Model.User
import com.example.SwallowMonthJM.Server.MasterApplication
import com.example.SwallowMonthJM.Unit.errorConvert
import com.example.SwallowMonthJM.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var backPressTime:Long = 0
    private lateinit var callback: OnBackPressedCallback

    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginButton.setOnClickListener {
            login()
        }
        binding.insertButton.setOnClickListener {
            val intent = Intent(applicationContext,InsertActivity::class.java)
            startActivity(intent)
        }
        //뒤로가기 조작 (2초 이내 연속 클릭 시 종료)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis()>backPressTime+2000){
                    backPressTime = System.currentTimeMillis()
                    Toast.makeText(applicationContext,"'뒤로' 버틀을 한번 더 누르시면 앱이 종료됩니다."
                        ,Toast.LENGTH_SHORT).show()
                }else{
                    finishAffinity()
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this,callback)
    }

    private fun login(){

        (application as MasterApplication).service.loginUser(
            getUserName(),getUserPass()
        ).enqueue(object :Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                (application as MasterApplication).createRetrofit()
                if (response.isSuccessful && response.body()!=null){

                    //로그인 성공
                    val user = response.body()
                    //토큰 가져오기
                    val token = user!!.token
                    if (token!=null){
                        saveUserToken(token,this@LoginActivity)
                    }
                    //다음 화면으로 넘어가기
                    val intent = Intent(applicationContext,MainActivity::class.java)
                    intent.putExtra("username",getUserName())
                    startActivity(intent)
                }else{
                    val err = errorConvert(response.errorBody())
                    Toast.makeText(this@LoginActivity,err,Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@LoginActivity,"Network error",Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }

    private fun saveUserToken(token:String,activity: Activity){
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp",token)
        editor.apply()
    }
    private fun getUserName() : String{
        return binding.userNameInput.text.toString()
    }
    private fun getUserPass() : String{
        return binding.userPasswordInput.text.toString()
    }
}