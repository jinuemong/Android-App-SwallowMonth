package com.example.SwallowMonthJM

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.SwallowMonthJM.databinding.ActivityToDoMainBinding
import com.example.SwallowMonthJM.databinding.ItemToDoBinding

@SuppressLint("NotifyDataSetChanged")
class ToDoMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityToDoMainBinding
    private val viewModel : ToDoMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToDoMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initRecyclerView()
        updateUI()
        binding.addButton.setOnClickListener {
            viewModel.addData(binding.editText.text.toString())
        }

    }
    private fun initRecyclerView(){
        binding.todoRecycler.apply {
            layoutManager = LinearLayoutManager(this@ToDoMainActivity)
            binding.todoRecycler.adapter = ToDoAdapter(
                ArrayList(),
                onClickDeleteButton = {
                    viewModel.delData(it) },
                onClickItem= {
                    viewModel.changeData(it) })
        }
    }
    //관찰 UI 업데이트
    private fun updateUI(){
        viewModel.todoLiveData.observe(this, Observer {
            (binding.todoRecycler.adapter as ToDoAdapter).setData(it)
        })
    }
}

data class Todo(
    val text : String,
    var isDone : Boolean,
)

class ToDoAdapter(
    private var dataSet : ArrayList<Todo>,
    val onClickDeleteButton:(todo:Todo)->Unit,
    val onClickItem : (todo:Todo)->Unit
    //unit : 리턴이 따로 없음
    //함수를 통해서 외부로 position 넘겨줌)
):RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){

    class ToDoViewHolder(val binding: ItemToDoBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_to_do,parent,false)
        return ToDoViewHolder(ItemToDoBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.itemText.text = dataSet[position].text

        if (dataSet[position].isDone){ //줄 긋기
            holder.binding.itemText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null,Typeface.ITALIC) //텍스트 변경
            }
        }else{
            holder.binding.itemText.apply {
                paintFlags = 0 //기존 값으로 변경
                setTypeface(null,Typeface.NORMAL)
            }
        }
        holder.binding.itemDelButton.setOnClickListener {
            //삭제 버튼 클릭 시 외부로 해당 데이터 전송
            onClickDeleteButton.invoke(dataSet[position])
        }
        holder.binding.itemText.setOnClickListener {
            //텍스트 클릭 시 밑 줄 긋기
            onClickItem.invoke(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size

    //데이터 변경 시 해당 메서드 실행
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData:ArrayList<Todo>){
        dataSet = newData
        notifyDataSetChanged()
    }
}

class ToDoMainViewModel : ViewModel(){

    var todoLiveData = MutableLiveData<ArrayList<Todo>>()
    //데이터를 여기서 관리함 - 뷰모델은 액티비티가 종료될때 종료
    private var todoData = ArrayList<Todo>()

    fun addData(text:String){
        todoData.add(Todo(text, false))
        todoLiveData.value = todoData
    }

    fun delData(todo: Todo){
        todoData.remove(todo)
        todoLiveData.value = todoData
    }
    fun changeData(todo:Todo){
        todo.isDone = !todo.isDone
        todoLiveData.value = todoData
    }
}