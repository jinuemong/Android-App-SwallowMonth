package com.example.SwallowMonthJM.Calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.Unit.SelectIconDialog
import com.example.SwallowMonthJM.Unit.Task
import com.example.SwallowMonthJM.Unit.calendarIcon
import com.example.SwallowMonthJM.databinding.ItemToDoCalendarBinding
import com.example.SwallowMonthJM.databinding.SlideLayoutCalendarBinding

class CalendarSlider(
    slideLayout: SlideLayoutCalendarBinding,
    private val mainActivity: MainActivity,
    val addData:()->Unit,
    val delData:()->Unit
) {
    private lateinit var keyDay:String
    private val topTextView = slideLayout.todoTopText
    private val editTypingView = slideLayout.todoEditText
    private val addButton = slideLayout.todoAddButton
    private val goAllGoalsButton = slideLayout.todoViewAllGoals
    private val goAddRoutineButton = slideLayout.todoAddRoutine
    private val recentlyListRecycler = slideLayout.todoRecentlyList

    fun initView(keyData: String){
        keyDay = keyData
        val topText = "$keyDay 일정"
        topTextView.text = topText

        setUpListener()
        initRecyclerView()
        updateUI()
    }

    private fun setUpListener(){
        goAllGoalsButton.setOnClickListener {
            mainActivity.viewPager.currentItem= 1
        }
        goAddRoutineButton.setOnClickListener {
            mainActivity.viewPager.currentItem = 2
        }

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
            mainActivity.viewModel.addTodoData(keyDay, editTypingView.text.toString(),0)
            editTypingView.setText("")
            addData()
        }
        //바 내리기
        val imm =
            mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainActivity.currentFocus?.windowToken, 0)
    }

    private fun initRecyclerView(){
        recentlyListRecycler.apply {
            layoutManager = LinearLayoutManager(mainActivity)
            val keyData =
                if(mainActivity.viewModel.todoData[keyDay]==null) ArrayList<Task>()
                else mainActivity.viewModel.todoData[keyDay]!!

            adapter = ToDoCalendarAdapter(
                mainActivity,
                keyData,
                onClickDeleteButton = {
                    mainActivity.viewModel.delTodoData(keyDay, it)

                    if(mainActivity.viewModel.todoData[keyDay]!!.size==0){
                        mainActivity.viewModel.todoData.remove(keyDay)
                        delData()
                    }
                },
                onClickItem = {
                    mainActivity.viewModel.doneData(it)
                },
                onClickChangeIcon = { todo,index->
                    mainActivity.viewModel.setIcon(todo,index)
                }
            )
        }
    }

    //observer 등록
    private fun updateUI(){
        mainActivity.viewModel.recentlyAddData.observe(mainActivity, Observer {
            it[keyDay]?.let { keyData ->
                (recentlyListRecycler.adapter as ToDoCalendarAdapter).setData(keyData)
            }
        })
    }
}

class ToDoCalendarAdapter(
    private val mainActivity : MainActivity,
    private var dataSet: ArrayList<Task>,
    val onClickDeleteButton:(todo: Task)->Unit,
    val onClickItem : (todo:Task)->Unit,
    val onClickChangeIcon : (todo:Task, index:Int)->Unit,
    //return 값이 없는 Unit을 넘겨줌 외부로 position 넘겨주는 동작
):RecyclerView.Adapter<ToDoCalendarAdapter.ToDoCalendarViewHolder>(){

    class ToDoCalendarViewHolder(val binding: ItemToDoCalendarBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoCalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_to_do_calendar,parent,false)
        return ToDoCalendarViewHolder(ItemToDoCalendarBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ToDoCalendarViewHolder, position: Int) {
        holder.binding.calendarItemText.text = dataSet[position].text
        holder.binding.calendarItemIcon.setImageResource(calendarIcon[dataSet[position].iconType])

        //icon 변경
       holder.binding.calendarItemIcon.setOnClickListener {
           val dig = SelectIconDialog(mainActivity)
           dig.showDig()

           dig.setOnClickedListener(object :SelectIconDialog.ButtonClickListener{
               override fun onClicked(index: Int?) {
                   if (index!=null){
                       onClickChangeIcon.invoke(dataSet[holder.bindingAdapterPosition],index)
                   }
               }

           })
        }

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

        //삭제
        holder.binding.calendarItemDel.setOnClickListener {
            onClickDeleteButton.invoke(dataSet[position])
        }


    }

    override fun getItemCount(): Int = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData:ArrayList<Task>){
        dataSet = newData
        notifyDataSetChanged()
    }

}