package com.example.SwallowMonthJM.Calendar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.example.SwallowMonthJM.R

class MonthPickerDialog(
    private val year:Int,
    private val month:Int,
) :DialogFragment() {

    var btnSet: Button? = null
    var btnCancel: Button? = null
    var monthPicker: NumberPicker? = null
    var yearPicker : NumberPicker? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder  = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val dialog : View = inflater.inflate(R.layout.month_picker,null).also {
            btnSet = it.findViewById<Button>(R.id.btn_set)
            btnCancel = it.findViewById<Button>(R.id.btn_cancel)
            monthPicker = it.findViewById<View>(R.id.month_picker) as NumberPicker
            yearPicker = it.findViewById<View>(R.id.year_picker) as NumberPicker
        }
        btnSet?.setOnClickListener{
            onClickedListener.onClicked(yearPicker!!.value,monthPicker!!.value)
            dismiss()
        }

        btnCancel?.setOnClickListener{
            dismiss()
        }

        monthPicker?.apply {
            minValue = 1
            maxValue = 12
            value = month
        }
        yearPicker?.apply {
            minValue = 2010
            maxValue = 2099
            value = year
        }
        builder.setView(dialog)
        return builder.create()
    }

    interface ButtonClickListener {
        fun onClicked(year:Int,month:Int)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }
}