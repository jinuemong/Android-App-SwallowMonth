package com.jinuemong.SwallowMonthJM.Server.TokenManager


//로그아웃 기능을 하는 api
//토큰 재 요청시 null값이 오면 사용자 로그아웃 진행
//  SafeApiCall 응답용으로만 일단 구현
abstract class BaseRepository() : SafeApiCall