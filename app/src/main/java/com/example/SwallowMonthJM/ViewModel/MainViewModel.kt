package com.example.SwallowMonthJM.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Unit.Profile
import com.example.SwallowMonthJM.Unit.Todo

//유저 프로필, 리스트 관리
class MainViewModel : ViewModel(){
    private val userProfile: Profile? = null

    var recentlyAddData = MutableLiveData<HashMap<String,ArrayList<Todo>>>()
    var todoData = HashMap<String, ArrayList<Todo>>()

    ///////////arrayList에서 key로 받는것이 중요 !

    fun addTodoData(key:String,text:String){
        todoData.put(key,Todo(null,key,text,false))
        Log.d("recentlyAddData",key+"   "+text)
        Log.d("recentlyAddData",todoData.toString())
        recentlyAddData.value = todoData
    }

    fun delTodoData(key:String,todo:Todo){
        todoData[key]?.remove(todo)
        recentlyAddData.value = todoData
    }

    fun doneData(todo:Todo){
        todo.isDone = !todo.isDone
        recentlyAddData.value = todoData
    }
}