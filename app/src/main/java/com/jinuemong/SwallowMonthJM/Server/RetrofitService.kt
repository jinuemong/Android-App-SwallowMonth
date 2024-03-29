package com.jinuemong.SwallowMonthJM.Server

import com.jinuemong.SwallowMonthJM.Model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    ): Call<GetUser>

    // 가입
    @POST("user/register/")
    @FormUrlEncoded
    fun registerUser(
        @Field("userName") userName: String,
        @Field("password") password: String
    ): Call<GetUser>

    //비밀번호 수정
    @PATCH("user/current/")
    @FormUrlEncoded
    fun updatePassword(
        @Field("userName") userName: String,
        @Field("password") password: String
    ):Call<User>



    // 유저 얻기
    @GET("user/profile/{profileId}/")
    fun getProfile(
        @Path("profileId") profileId : Int
    ): Call<Profile>

    //유저 검색
    @FormUrlEncoded
    @POST("user/search/profile/")
    fun searchProfile(
        @Field("search") search : String
    ) : Call<ArrayList<Profile>>
    // profile 수정
    @Multipart
    @POST("user/update/profile/")
    fun setUserProfile(
        @Part("profileId") profileId: RequestBody,
        @Part("userName") userName: RequestBody,
        @Part("userComment") userComment: RequestBody,
        @Part userImage: MultipartBody.Part?,
    ) : Call<Profile>

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
    @DELETE("task/tasks/{id}/")
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
    @DELETE("routine/routines/{routineId}/")
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


    //Patch Data

    //Month 수정
    @PATCH("month/monthDatas/{monthId}/")
    fun setMonthData(
        @Path("monthId")monthId: Int,
        @Body monthData: MonthData
    ):Call<MonthData>

    //Task 수정
    @PATCH("task/tasks/{id}/")
    fun setTaskData(
        @Path("id")id : Int,
        @Body task : Task
    ):Call<Task>

    //Routine 수정
    @PATCH("routine/routines/{routineId}/")
    fun setRoutineData(
        @Path("routineId")routineId : Int,
        @Body routine : Routine
    ):Call<Routine>

    //dayRoutine 수정
    @PATCH("routine/dayRoutines/{id}/")
    fun setDayRoutineData(
        @Path("id")id : Int,
        @Body dayRoutine : DayRoutine
    ):Call<DayRoutine>


    ////Relation//

    // add FriendShip
    @POST("relation/friendShips/")
    @FormUrlEncoded
    fun addFriendShip(
        @Field("name") name : String,
    ):Call<FriendShip>

    //add FUser
    @POST("relation/fusers/")
    @FormUrlEncoded
    fun addFUser(
        @Field("frId") frId:Int,
        @Field("userId") userId:String,
        @Field("otherUser") otherUser:Int,
    ):Call<FUser>

    // 친구 관계 얻기
    @GET("relation/friendShips/{frId}/")
    fun getFriendShip(
        @Path("frId") frId: Int,
    ):Call<FriendShip>

    //친구 관계 삭제 시 친구 리스트 자동 삭제
    @DELETE("relation/friendShips/{frId}/")
    fun delFriendShip(
        @Path("frId") frId: Int,
    ):Call<FriendShip>

    // 내 친구 리스트
    @POST("relation/friends/")
    @FormUrlEncoded
    fun getFriends(
        @Field("userName")userName : String,
        @Field("num") number : Int
    ):Call<FriendList>

    //친구 확인
    @POST("relation/checkFriendShip/")
    @FormUrlEncoded
    fun checkFriendShip(
        @Field("userName")userName: String,
        @Field("targetUser")targetUser : Int,
    ):Call<CheckRelation>

    //랜덤 유저 얻기
    @POST("relation/randomProfile/")
    @FormUrlEncoded
    fun getRandomProfile(
        @Field("profileId")profileId : Int,
    ):Call<ArrayList<Profile>>

    //내 알림 리스트 얻기
    @GET("relation/alarms/")
    fun getAlarmList(
        @Query("userName") userName: String,
    ): Call<ArrayList<AlarmForGet>>

    // 알림 보내기
    @POST("relation/alarms/")
    @FormUrlEncoded
    fun addAlarm(
        @Field("userId")userName: String,
        @Field("type")type:String,
        @Field("typeId")typeId : Int,
        @Field("fromUserId")fromUserId:String,
    ): Call<Alarm>

    @DELETE("relation/alarms/{alarmId}/")
    fun delAlarm(
        @Path("alarmId")alarmId:Int,
    ): Call<Alarm>


    //메시지

    //생성
    @POST("relation/messages/")
    @FormUrlEncoded
    fun postMessage(
        @Field("frId")frId: Int,
        @Field("userId")userId : String,
        @Field("text")text : String,
    ):Call<Message>

    // 삭제
    @DELETE("relation/messages/{messageId}/")
    fun deleteMessage(
        @Path("messageId")messageId:Int,
    ):Call<Message>

    //메시지 리스트 받기
    @GET("relation/messages/")
    fun getMessageList(
        @Query(value = "frId")frId:Int,
    ):Call<ArrayList<Message>>

    //채팅 방 리스트 받기
    @POST("relation/messageList/")
    @FormUrlEncoded
    fun getMessageRoomList(
        @Field("userName")userName: String
    ):Call<ArrayList<FriendData>>

    // 레코드
    @GET("month/recordDatas/")
    fun getRecordDataList(
        @Query(value = "userName", encoded = true) userName :String,
    ): Call<ArrayList<RecordData>>

    //최근 레코드 구하기 (1)
    @GET("month/recordDatas/")
    fun getRecentlyData(
        @Query(value = "userName", encoded = true) userName :String,
        @Query(value = "keyDate", encoded = true) keyDate :String,
    ): Call<ArrayList<RecordData>>

    // 랭킹
    @GET("month/rankingDatas/")
    fun getRankingDataList(
        @Query(value = "search", encoded = true) keyDate :String
    ): Call<ArrayList<RecordData>>


}