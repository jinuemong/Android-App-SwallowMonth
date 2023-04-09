package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.MonthDataManager
import com.example.SwallowMonthJM.Manager.TaskManager
import com.example.SwallowMonthJM.Model.Task
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
    private val taskManager = TaskManager(mainActivity.masterApp)
    private val monthManager = MonthDataManager(mainActivity.masterApp)
    var taskLiveData = MutableLiveData<ArrayList<Task>>()
    var taskArrayList = ArrayList<Task>()
    var currentTaskList = MutableLiveData<ArrayList<Task>>() //현재 뷰 리스트
    var currentDoneList = MutableLiveData<ArrayList<Task>>() //현재 완료 리스트
    init {
        taskLiveData.value = taskArrayList
        currentTaskList.value = arrayListOf()
        currentDoneList.value = arrayListOf()
    }


    private var addData = ArrayList<Task>()

    // 달력에 일정 추가
    fun addTaskData(startNum:Int,endNum:Int,task: Task){
        for (i in startNum..endNum){
            val newTask = SampleTask(task).deepCopy()
            newTask.task.dayIndex=i
            taskManager.addTaskData(newTask.task, paramFun = {
                if (it != null) {
                    addData.add(it)
                }
            })
        }
        Thread.sleep(500)

        //추가한 데이터가 0보다 크고 추가한 데이터의 달이 현재 달과 같은 경우
        if(addData.size>0 && addData[0].monthId==mainView.monthData.monthId){
            mainView.monthData.apply {
                taskCount+=addData.size
                totalPer = (((doneTask+clearRoutine).toDouble()/(taskCount+dayRoutineCount).toDouble())*100.0).toInt()
                monthId?.let { id->
                    monthManager.setMonthData(id,this, paramFun = {})
                }
            }
            taskArrayList.addAll(addData)
            taskLiveData.postValue(taskArrayList)
        }
        addData.clear()
    }

    fun delTaskData(task: Task){
        task.id?.let {
            taskManager.delTaskData(it, paramFun = {})
            if (task.isDone){
                mainView.monthData.apply {
                    doneTask-=1
                    taskCount-=1
                    totalPer = (((doneTask+clearRoutine).toDouble()/(taskCount+dayRoutineCount).toDouble())*100.0).toInt()
                    monthId?.let { id->
                        monthManager.setMonthData(id,this, paramFun = {})
                    }
                }
            }
        }
        taskArrayList.remove(task)
        taskLiveData.value = taskArrayList
    }

    fun doneTaskData(task : Task){
        if (!task.isDone) {
            task.isDone = true
            task.per = 100
            task.id?.let {  id ->
                taskManager.setTaskData(id,task, paramFun = {})
            }
            mainView.monthData.apply {
                totalPoint += levelPoint[task.level]
                doneTask+=1
                totalPer = (((doneTask+clearRoutine).toDouble()/(taskCount+dayRoutineCount).toDouble())*100.0).toInt()
                //서버에 적용
                this.monthId?.let { id->
                    monthManager.setMonthData(id,this, paramFun = {
                    })
                }
            }
            taskLiveData.value = taskArrayList
        }
    }

    fun setTaskICon(iconIndex:Int,task: Task){
        task.iconType = iconIndex
        task.id?.let {
            taskManager.setTaskData(it,task, paramFun = {})
        }
        taskLiveData.value = taskArrayList
    }

    //task 하나의 per 구하기
    fun setPerTask(task: Task, p:Int){
        task.per = p
        task.id?.let {
            taskManager.setTaskData(it,task, paramFun = {})
        }
        taskLiveData.value = taskArrayList
    }



}