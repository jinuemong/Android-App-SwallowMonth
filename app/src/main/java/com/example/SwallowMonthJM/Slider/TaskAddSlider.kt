package com.example.SwallowMonthJM.Slider

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.databinding.SlideLayoutAddTaskBinding

class TaskAddSlider(
    slideLayout: SlideLayoutAddTaskBinding,
    private val mainActivity: MainActivity,
    val addData:(text:String)->Unit
) {
    private val editTypingView = slideLayout.todoEditText
    private val addButton = slideLayout.todoAddButton


    fun setUpListener(){
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

    private fun addTypeData(){
        if (editTypingView.text!=null && editTypingView.text.toString()!="") {
            addData(editTypingView.text.toString())
            editTypingView.setText("")
        }
        //바 내리기
        val imm =
            mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainActivity.currentFocus?.windowToken, 0)
    }

}