package com.example.SwallowMonthJM.Network

import com.example.SwallowMonthJM.Model.*
import retrofit2.Call
import retrofit2.http.*

//url 관리 
interface RetrofitService {

    //로그인 요청
    @POST("user/login/")
    @FormUrlEncoded
    fun loginUser(
        @Field("userName") userName: String,
        @Field("password") password: String
    ): Call<User>

    // 가입
    @POST("user/register/")
    @FormUrlEncoded
    fun registerUser(
        @Field("userName") userName: String,
        @Field("password") password: String
    ): Call<User>

    // 유저 조회
    @GET("user/profile/")
    fun getProfile(
        @Query(value = "search", encoded = true) userName: String
    ): Call<ArrayList<Profile>>

    // 전체 monthData 받기
    @GET("month/monthDatas/")
    fun getMonthDataList(
        @Query(value = "userName", encoded = true) userName: String
    ): Call<ArrayList<MonthData>>

    //특정 monthData 받기 (1개)
    @GET("month/monthDatas/")
    fun getMonthKeyDate(
        @Query(value = "userName", encoded = true) userName: String,
        @Query(value = "keyDate", encoded = true) keyDate: String
    ): Call<ArrayList<MonthData>>

    //Month Data 추가
    @POST("month/monthDatas/")
    @FormUrlEncoded
    fun makeMonthData(
        @Field("userId") userId: String,
        @Field("keyDate") keyDate: String,
        @Field("totalPer") totalPer: Int,
        @Field("totalPoint") totalPoint: Int,
        @Field("taskCount") taskCount: Int,
        @Field("dayRoutineCount") dayRoutineCount: Int,
        @Field("doneTask") doneTask: Int,
        @Field("clearRoutine") clearRoutine: Int,
    ): Call<MonthData>

    //Month Data 삭제
    @DELETE("month/monthDatas/{monthId}/")
    fun delMonthData(
        @Path("monthId") monthId: Int,
    ): Call<MonthData>

    //Task 추가
    @POST("task/tasks/")
    @FormUrlEncoded
    fun addTask(
        @Field("monthId") monthId: Int,
        @Field("userId") userId: String,
        @Field("dayIndex") dayIndex: Int,
        @Field("text") text: String,
        @Field("isDone") isDone: Boolean,
        @Field("iconType") iconType: Int,
        @Field("level") level: Int,
        @Field("per") per: Int,
    ): Call<Task>

    //Task 제거
    @DELETE("task/tasks/{id}")
    fun delTask(
        @Path("id") id: Int,
    ): Call<Task>

    //month task 얻기
    @GET("task/tasks/")
    fun getDayTaskList(
        @Query(value = "userName", encoded = true) userName: String,
        @Query(value = "monthId", encoded = true) monthId: Int,
        ): Call<ArrayList<Task>>

    //Routine 추가
    @POST("routine/routines/")
    @FormUrlEncoded
    fun addRoutine(
        @Field("userId") userId: String,
        @Field("monthId") monthId: Int,
        @Field("keyDate") keyDate: String,
        @Field("text") text: String,
        @Field("cycle") cycle: Int,
        @Field("startNum") startNum: Int,
        @Field("totalRoutine") totalRoutine: Int,
        @Field("clearRoutine") clearRoutine: Int,
        @Field("iconType") iconType: Int,
        @Field("topText") topText: String,
    ): Call<Routine>

    //Routine 제거 routineId
    @DELETE("routine/routines/{routineId}")
    fun delRoutine(
        @Path("routineId") routineId: Int,
    ): Call<Routine>

    //dayRoutine 추가
    @POST("routine/dayRoutines/")
    @FormUrlEncoded
    fun addDayRoutine(
        @Field("routineId") routineId: Int,
        @Field("monthId") monthId: Int,
        @Field("dayIndex") dayIndex: Int,
        @Field("clear") clear: Boolean,
    ): Call<DayRoutine>

    //month routine 얻기
    @GET("routine/routines/")
    fun getMonthRoutineList(
        @Query(value = "userName", encoded = true) userName: String,
        @Query(value = "keyDate", encoded = true) keyDate: String,
    ): Call<ArrayList<Routine>>
}