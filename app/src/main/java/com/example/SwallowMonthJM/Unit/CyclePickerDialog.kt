package com.example.SwallowMonthJM.Unit

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R

class CyclePickerDialog(
    private val mainActivity: MainActivity,
    private val  remainingDays:Int
) :DialogFragment(){
    var topTextInPicker = ""
    var btnSet: Button? = null
    var btnCancel: Button? = null
    var cyclePicker: NumberPicker? = null
    var text : TextView?= null

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val dialog : View = inflater.inflate(R.layout.clycle_picker,null).also {
            btnSet= it.findViewById(R.id.btn_set)
            btnCancel = it.findViewById<Button>(R.id.btn_cancel)
            cyclePicker = it.findViewById<View>(R.id.circle_picker) as NumberPicker
            text = it.findViewById(R.id.mid_text)
        }
        btnSet?.setOnClickListener{
            onClickedListener.onClicked()
            val cycleInPic = cyclePicker!!.value

            mainActivity.addViewModel.apply {
                cycle = cycleInPic
                totalRoutine = remainingDays / cycleInPic
                topText = topTextInPicker
                Log.d("topText",topText)
                routineChange.value =routineChange.value != true
            }
            dismiss()
        }
        btnCancel?.setOnClickListener {
            dismiss()
        }
        cyclePicker?.apply {
            minValue=1
            maxValue= remainingDays
            value = 1
        }
        cyclePicker?.setOnValueChangedListener{_,_,i2->
            val routineDay = remainingDays/i2
            topTextInPicker = "total of $remainingDays days and $routineDay routines"
            text?.text = topTextInPicker
        }
        builder.setView(dialog)
        return builder.create()
    }

    interface ButtonClickListener{
        fun onClicked()
    }
    private lateinit var onClickedListener:ButtonClickListener

    fun setOnClickedListener(listener:ButtonClickListener){
        onClickedListener = listener
    }
}