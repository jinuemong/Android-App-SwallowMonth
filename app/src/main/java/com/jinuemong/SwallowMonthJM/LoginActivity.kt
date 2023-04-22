package com.jinuemong.SwallowMonthJM

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.jinuemong.SwallowMonthJM.Model.GetUser
import com.jinuemong.SwallowMonthJM.Model.Token
import com.jinuemong.SwallowMonthJM.Server.MasterApplication
import com.jinuemong.SwallowMonthJM.Unit.errorConvert
import com.jinuemong.SwallowMonthJM.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var backPressTime:Long = 0
    private lateinit var callback: OnBackPressedCallback
    private lateinit var binding:ActivityLoginBinding
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : Editor
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // on create 내부에 선언
        val masterKeyAlias = MasterKey
            .Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences  =
            EncryptedSharedPreferences.create(
                applicationContext,
                "encrypted_settings", //파일이름
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        editor = sharedPreferences.edit()


        val isLogout = intent.getBooleanExtra("logout",false)
        //로그아웃으로 넘어왔다면 자동 로그인 삭제
        if (isLogout){
            //데이터가 존재하면 삭제
            checkSharedLogin()
        }

        //자동 로그인 체크 시 로그인
        if (checkAutoData()){
            binding.autoLoginCheckBox.isChecked = true
            setUserName(sharedPreferences.getString("name",""))
            setUserPass(sharedPreferences.getString("pass",""))
            login()
        }

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
        val masterApp = (application as MasterApplication)
        masterApp.createRetrofit(this@LoginActivity)
        masterApp.service.loginUser(
            getUserName(),getUserPass()
        ).enqueue(object :Callback<GetUser>{
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                if (response.isSuccessful && response.body()!=null){

                    //로그인 성공
                    val authUser = response.body()!!.user

                    //토큰 가져오기
                    val token = authUser.token
                    saveUserToken(token,this@LoginActivity)

                    //자동 로그인 확인
                    checkSharedLogin()

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

            override fun onFailure(call: Call<GetUser>, t: Throwable) {
                Toast.makeText(this@LoginActivity,"Network error",Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }

    private fun saveUserToken(token: Token, activity: Activity){
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("accessToken", token.access)
        editor.putString("refreshToken", token.refresh)
        editor.apply()
    }
    private fun getUserName() : String{
        return binding.userNameInput.text.toString()
    }
    private fun getUserPass() : String{
        return binding.userPasswordInput.text.toString()
    }

    private fun setUserName(name : String?){
        if(name!="") {
            binding.userNameInput.setText(name)
        }
    }

    private fun setUserPass(pass : String?){
        if(pass!="") {
            binding.userPasswordInput.setText(pass)
        }
    }

    //자동 로그인 체크
    private fun checkSharedLogin(){
        //체크된 상태 확인
        if(binding.autoLoginCheckBox.isChecked){
            if (!checkAutoData()){
                setAutoData()
            }
        //해제
        }else{
            if (checkAutoData()) {
                removeAutoData()
            }
        }
    }

    //자동 로그인 저장
    private fun setAutoData(){
        editor.putString("name",getUserName())
        editor.putString("pass",getUserPass())
        editor.commit()
    }
    //자동 로그인 삭제
    private fun removeAutoData(){
        editor.remove("name")
        editor.remove("pass")
        editor.clear()
        editor.commit()
    }

    //아이디와 비밀번호가 존재하는지 확인
    private fun checkAutoData() :Boolean{
        return sharedPreferences.getString("name","")!="" &&
                sharedPreferences.getString("pass","")!=""
    }
}