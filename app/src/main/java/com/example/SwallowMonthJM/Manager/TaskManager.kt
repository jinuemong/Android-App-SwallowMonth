package com.example.SwallowMonthJM.Manager

import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.Server.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TaskManager(
    private val materApp : MasterApplication
) {

    fun getTaskList(userName:String,monthId:Int,paramFun: (ArrayList<Task>?) -> Unit){
        materApp.service.getDayTaskList(userName,monthId)
            .enqueue(object :Callback<ArrayList<Task>>{
                override fun onResponse(
                    call: Call<ArrayList<Task>>,
                    response: Response<ArrayList<Task>>
                ) {
                    if (response.isSuccessful){
                        paramFun(response.body())
                    }else {
                        paramFun(null)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Task>>, t: Throwable) {
                    paramFun(null)
                }

            })
    }

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

    fun setTaskData(taskId: Int, task: Task, paramFun: (Task?) -> Unit) {
        materApp.service.setTaskData(taskId, task).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.isSuccessful){
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