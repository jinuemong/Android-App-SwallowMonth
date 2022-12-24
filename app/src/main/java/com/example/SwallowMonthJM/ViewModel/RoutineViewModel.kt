package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.Model.DayRoutine
import com.example.SwallowMonthJM.Model.Routine

class RoutineViewModel(
    mainActivity: MainActivity
): ViewModel(){
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

    fun doneRoutineData(dayRoutine: DayRoutine){
        dayRoutine.clear = true
        routineLivData.value = currentRoutineArr
    }
}