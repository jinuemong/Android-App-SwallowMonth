package com.example.SwallowMonthJM.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SwallowMonthJM.Model.Routine

class RoutineViewModel: ViewModel(){
    var routineLivData = MutableLiveData<ArrayList<Routine>>()

    private var currentRoutineArr = ArrayList<Routine>()

    fun addRoutineData(routine:Routine){
        currentRoutineArr.add(routine)

        routineLivData.value = currentRoutineArr
    }
}