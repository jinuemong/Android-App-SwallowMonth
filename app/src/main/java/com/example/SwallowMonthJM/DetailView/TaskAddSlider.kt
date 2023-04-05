package com.example.SwallowMonthJM.DetailView

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.SlideLayoutAddTaskBinding

class TaskAddSlider(
    slideLayout: SlideLayoutAddTaskBinding,
    private val mainActivity: MainActivity,
    val addData:(text:String)->Unit
) {
    private val editTypingView = slideLayout.todoEditText
    private val addButton = slideLayout.todoAddButton


    fun setUpListener(){
        editTypingView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editTypingView.setBackgroundColor(0)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        //버튼 리스너
        editTypingView.setOnKeyListener { v, keyCode, event ->
            var handled = false

            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                addTypeData()
                handled = true
            }
            addButton.setOnClickListener {
                addTypeData()
                handled = true
            }
            handled
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun addTypeData(){
        if (editTypingView.text!=null && editTypingView.text.toString()!="") {
            addData(editTypingView.text.toString())
            editTypingView.setBackgroundColor(R.color.gray)
        }
        //바 내리기
        val imm =
            mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainActivity.currentFocus?.windowToken, 0)
    }

}