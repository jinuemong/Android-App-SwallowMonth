package com.example.SwallowMonthJM.Unit

import android.app.Dialog
import android.view.MotionEvent
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.Adapter.IconAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.SelectIconDialogBinding

class SelectIconDialog(
    private val context: AppCompatActivity
) {

    private lateinit var binding: SelectIconDialogBinding
    private val dig = Dialog(context)

    fun showDig() {
        binding = SelectIconDialogBinding.inflate(context.layoutInflater)
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀 제거
        dig.setContentView(binding.root)
        dig.setCancelable(true)
        dig.window?.setBackgroundDrawableResource(R.drawable.round_border)

        binding.iconSelectRecycler.apply {
            adapter = IconAdapter(context as MainActivity)
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        val position = rv.getChildAdapterPosition(child)
                        val view = rv.layoutManager?.findViewByPosition(position)
                        view?.setOnClickListener {
                            onClickedListener.onClicked(position)
                            dig.dismiss()
                        }
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }

        dig.show()

    }

    interface ButtonClickListener {
        fun onClicked(index: Int?)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }
}
