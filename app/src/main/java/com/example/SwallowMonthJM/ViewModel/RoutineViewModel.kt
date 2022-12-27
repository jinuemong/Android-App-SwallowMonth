package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayRoutine
import com.example.SwallowMonthJM.Model.Routine

class RoutineViewModel(
    mainActivity: MainActivity
): ViewModel(){
    class Factory(val mainActivity: MainActivity) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RoutineViewModel(mainActivity) as T
        }
    }
    private val mainView = mainActivity.viewModel

    var routineLivData = MutableLiveData<ArrayList<Routine>>()

    private var currentRoutineArr = ArrayList<Routine>()
    init {
        routineLivData.value = currentRoutineArr
    }
    fun addRoutineData(routine:Routine){
        for (i in 0 until routine.totalRoutine){
            val dayIndex = routine.startNum+i*routine.cycle
            routine.dayRoutineList[dayIndex] = DayRoutine(dayIndex,false)
        }
        currentRoutineArr.add(routine)
        routineLivData.value = currentRoutineArr
    }

    fun delRoutineData(routine: Routine){
        currentRoutineArr.remove(routine)
        routineLivData.value = currentRoutineArr
    }

    fun doneRoutineData(routine: Routine,dayRoutine: DayRoutine){
        if(!dayRoutine.clear) {
            routine.clearRoutine += 1
        }
        dayRoutine.clear = true
        routineLivData.value = currentRoutineArr
    }
}