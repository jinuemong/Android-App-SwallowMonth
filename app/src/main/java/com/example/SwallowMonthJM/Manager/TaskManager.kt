package com.example.SwallowMonthJM.Manager

import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.Server.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TaskManager(
    private val materApp : MasterApplication
) {

    fun addTaskData(task: Task,paramFun :(Task?)->Unit){
        materApp.service.addTask(task.monthId,task.userId,task.dayIndex,task.text,task.isDone
        ,task.iconType,task.level,task.per).enqueue(object : Callback<Task>{
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.isSuccessful && response.body()!=null){
                    paramFun(response.body())
                }else{
                    paramFun(null)
                }
            }
            override fun onFailure(call: Call<Task>, t: Throwable) {
                paramFun(null)
            }
        })
    }

    fun delTaskData(taskId:Int,paramFun: (Task?) -> Unit){
        materApp.service.delTask(taskId).enqueue(object : Callback<Task>{
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.isSuccessful && response.body()!=null){
                    paramFun(response.body())
                }else{
                    paramFun(null)
                }
            }
            override fun onFailure(call: Call<Task>, t: Throwable) {
                paramFun(null)
            }
        })
    }
}