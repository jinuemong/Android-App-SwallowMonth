package com.example.SwallowMonthJM.Calendar

import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.Todo
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.ItemToDoCalendarBinding
import com.example.SwallowMonthJM.databinding.SlideLayoutCalendarBinding

class CalendarSlider(
    slideLayout: SlideLayoutCalendarBinding,
    private val mainActivity: MainActivity
) {
    private val topTextView = slideLayout.todoTopText
    private val editTypingView = slideLayout.todoEditText
    private val addButton = slideLayout.todoAddButton
    private val goAllGoalsButton = slideLayout.todoViewAllGoals
    private val goAddRoutineButton = slideLayout.todoAddRoutine
    private val recentlyListRecycler = slideLayout.todoRecentlyList

    fun initView(dateTime: String,day: String){
        val topText = dateTime+" "+ day + "일 일정"
        topTextView.text = topText
    }

    fun setUpListener(){
        goAllGoalsButton.setOnClickListener {
            mainActivity.viewPager.currentItem= 1
        }
        goAddRoutineButton.setOnClickListener {
            mainActivity.viewPager.currentItem = 2
        }
    }
}

class ToDoCalendarAdapter(
    val onClickDeleteButton:(todo: Todo)->Unit,
    val onClickItem : (todo:Todo)->Unit,
    //return 값이 없는 Unit을 넘겨줌 외부로 position 넘겨주는 동작
):RecyclerView.Adapter<ToDoCalendarAdapter.ToDoCalendarViewHolder>(){
    private val dataSet =  ArrayList<Todo>()

    class ToDoCalendarViewHolder(val binding: ItemToDoCalendarBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoCalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_to_do_calendar,parent,false)
        return ToDoCalendarViewHolder(ItemToDoCalendarBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ToDoCalendarViewHolder, position: Int) {
        holder.binding.calendarItemText.text = dataSet[position].text
        holder.binding.calendarItemIcon.setImageDrawable(calendarIcon[0].toDrawable())

        //onClickItem : unit = position 보내주기
        holder.binding.calendarItemText.setOnClickListener {
            onClickItem.invoke(dataSet[position])
        }

        //텍스트 줄 설정
        if (dataSet[position].isDone){
            holder.binding.calendarItemText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC) //텍스트 변경
            }
        }else{
            holder.binding.calendarItemText.apply {
                paintFlags = 0 //기존 값으로 변경
                setTypeface(null,Typeface.NORMAL)
            }
        }


    }

    override fun getItemCount(): Int = dataSet.size


}