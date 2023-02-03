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
                        routineData.monthId,
                        dayIndex, false)

                    routineManager.addDayRoutine(dayRoutine, paramFun = { dayRou->
                        routineData.dayRoutinePost.add(dayRou!!)
                    })
                }
                Thread.sleep(500)

                //추가한 데이터의 달이 현재 달과 같은 경우 추가
                if(routineData.monthId==mainView.monthData.monthId) {
                    currentRoutineArr.add(routineData)
                    routineLivData.postValue(currentRoutineArr)
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


}