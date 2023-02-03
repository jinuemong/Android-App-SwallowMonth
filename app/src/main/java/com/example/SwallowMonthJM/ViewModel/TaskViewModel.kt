package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.TaskManager
import com.example.SwallowMonthJM.Model.Task
import com.example.SwallowMonthJM.Network.MasterApplication
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

    var taskLiveData = MutableLiveData<ArrayList<Task>>()
    var currentTaskArr = ArrayList<Task>()
    init {
        taskLiveData.value = currentTaskArr
    }


    private var addData = ArrayList<Task>()

    // 달력에 일정 추가
    fun addTaskData(startNum:Int,endNum:Int,task: Task){
        for (i in startNum..endNum){
            val newTask = SampleTask(task).deepCopy()
            newTask.task.dayIndex=i
            taskManager.addTaskData(newTask.task, paramFun = {
                newTask.task = it!!
                currentTaskArr.add(newTask.task)
                addData.add(newTask.task)
            })
        }
        Thread.sleep(500)

        //추가한 데이터가 0보다 크고 추가한 데이터의 달이 현재 달과 같은 경우
        if(addData.size>0 && addData[0].monthId==mainView.monthData.monthId){
            currentTaskArr.addAll(addData)
            taskLiveData.postValue(currentTaskArr)
        }
        addData.clear()
    }

    fun delTaskData(task: Task){
        taskLiveData.value?.remove(task)
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