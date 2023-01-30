package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.TaskManager
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.Server.MasterApplication
import com.example.SwallowMonthJM.Unit.SampleTask
import com.example.SwallowMonthJM.Unit.levelPoint

class TaskViewModel(
     mainActivity: MainActivity
) : ViewModel() {
    class Factory(val mainActivity: MainActivity) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskViewModel(mainActivity) as T
        }
    }
    private val mainView = mainActivity.viewModel
    private val taskManager = TaskManager(mainActivity.application as MasterApplication)
    // 달력에 일정 추가
    fun addTaskData(startNum:Int,endNum:Int,task: Task){
        for (i in startNum..endNum){
            val newTask = SampleTask(task).deepCopy()
            newTask.task.dayIndex=i
            mainView.currentMonthArr[i].apply {
                if (this.taskList == null) {
                    this.taskList = ArrayList()
                }
                taskManager.addTaskData(newTask.task, paramFun = {
                    newTask.task = it!!
                    this.taskList!!.add(newTask.task)
                })
            }
        }
        mainView.dayLiveData.postValue(mainView.currentMonthArr)
    }

    fun delTaskData(task: Task){
        mainView.currentMonthArr[mainView.currentDayPosition.value!!]
            .taskList?.remove(task)
        task.id?.let { taskManager.delTaskData(it, paramFun = {}) }
        mainView.dayLiveData.postValue(mainView.currentMonthArr)
    }

    fun doneTaskData(task : Task){
        if (!task.isDone) {
            task.isDone = true
            task.per = 100
            mainView.dayLiveData.postValue(mainView.currentMonthArr)
            mainView.monthData.totalPoint+= levelPoint[task.level]
        }
    }

    fun setTaskICon(iconIndex:Int,task: Task){
        task.iconType = iconIndex
        mainView.dayLiveData.postValue(mainView.currentMonthArr)
    }

    fun setPerTask(task: Task, p:Int){
        task.per = p
        mainView.dayLiveData.postValue(mainView.currentMonthArr)
    }
}