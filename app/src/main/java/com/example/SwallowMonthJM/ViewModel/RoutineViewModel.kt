package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Manager.RoutineManager
import com.example.SwallowMonthJM.Model.DayRoutine
import com.example.SwallowMonthJM.Model.Routine
import com.example.SwallowMonthJM.Network.MasterApplication
import com.example.SwallowMonthJM.Unit.levelPoint

class RoutineViewModel(
    mainActivity: MainActivity
): ViewModel(){
    class Factory(val mainActivity: MainActivity) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RoutineViewModel(mainActivity) as T
        }
    }
    private val mainView = mainActivity.viewModel
    private val routineManager = RoutineManager(mainActivity.application as MasterApplication)
    var routineLivData = MutableLiveData<ArrayList<Routine>>()

    var currentRoutineArr = ArrayList<Routine>()
    init {
        routineLivData.value = currentRoutineArr
    }

    fun addRoutineData(routine:Routine){
        //서버에 생성하고 반환값으로 DayRoutine 생성해야함 !
        routineManager.addRoutine(routine, paramFun = { routineData->
            if (routineData!=null) {
                for (i in 0 until routineData.totalRoutine) {
                    val dayIndex = routineData.startNum + i * routineData.cycle
                    val dayRoutine = DayRoutine(
                        null, routineData.routineId!!,
                        mainView.monthData.monthId!!,
                        dayIndex, false)

                    routineManager.addDayRoutine(dayRoutine, paramFun = {
                        routineData.dayRoutinePost.add(it!!)
                        RoutineThread(routineData,"add").start()
                    })
                }
            }
        })
    }

    fun delRoutineData(routine: Routine){
        currentRoutineArr.remove(routine)
        routine.routineId?.let { routineManager.delRoutine(it, paramFun = {}) }
        routineLivData.postValue(currentRoutineArr)
    }

    fun doneRoutineData(routine: Routine,dayRoutine: DayRoutine){
        if(!dayRoutine.clear) {
            //one run
            routine.clearRoutine += 1
            mainView.monthData.totalPoint+= levelPoint[0]
        }
        dayRoutine.clear = true
        routineLivData.postValue(currentRoutineArr)
    }
    inner class RoutineThread(val routine:Routine,val type:String): Thread(){
        override fun run() {
            super.run()
            try {
                sleep(500)
                if (type=="add"){
                    currentRoutineArr.add(routine)
                    routineLivData.postValue(currentRoutineArr)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

}