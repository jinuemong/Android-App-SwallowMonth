package com.jinuemong.SwallowMonthJM.Unit

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.jinuemong.SwallowMonthJM.MainActivity
import com.jinuemong.SwallowMonthJM.R

class CyclePickerDialog(
    private val mainActivity: MainActivity,
    private val  remainingDays:Int
) :DialogFragment(){
    var topTextInPicker = "total of $remainingDays days and $remainingDays routines"
    var btnSet: Button? = null
    var btnCancel: Button? = null
    var cyclePicker: NumberPicker? = null
    var text : TextView?= null
    var routineDay = remainingDays
    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val dialog : View = inflater.inflate(R.layout.clycle_picker,null).also {
            btnSet= it.findViewById(R.id.btn_set)
            btnCancel = it.findViewById<Button>(R.id.btn_cancel)
            cyclePicker = it.findViewById<View>(R.id.circle_picker) as NumberPicker
            text = it.findViewById(R.id.mid_text)
            text?.text = topTextInPicker
        }
        btnSet?.setOnClickListener{
            onClickedListener.onClicked()
            val cycleInPic = cyclePicker!!.value

            mainActivity.addViewModel.apply {
                keyData = mainActivity.viewModel.currentDate.keyDate
                cycle = cycleInPic
                totalRoutine = routineDay
                topText = topTextInPicker
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
            routineDay = if (remainingDays%i2!=0){
                (remainingDays+1)/i2
            }else{
                remainingDays/i2
            }
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