package com.example.SwallowMonthJM.Unit

import com.example.SwallowMonthJM.R


// 아이콘 리스트
val calendarIcon :List<Int> = listOf(
    R.drawable.ic_iconmonstr_calendar_9,
    R.drawable.ic_baseline_directions_run_24,
    R.drawable.ic_baseline_sports_tennis_24,
    R.drawable.ic_iconmonstr_check_mark_13,
    R.drawable.ic_iconmonstr_pin_1,
    R.drawable.ic_iconmonstr_text_ckeck_lined,
    R.drawable.ic_baseline_hotel_class_24,
    R.drawable.ic_baseline_hiking_24,
    R.drawable.ic_baseline_groups_2_24,
    R.drawable.ic_baseline_directions_walk_24
)

//요일 리스트
val dayOfWeek : List<Int> = listOf(
    R.string.sun,
    R.string.mon,
    R.string.tue,
    R.string.wed,
    R.string.thu,
    R.string.fri,
    R.string.sat,
)

//레벨 리스트
val level : List<Int> = listOf(
    10,  //0 루틴
    10, //1  0 - 10분
    60, //2  10 - 60분
    180,//3  1시간~3시간
    360,//4  3시간~6시간
    500,//5  그 이상
)

val levelText:List<String> = listOf(
    "routine task",
    "about 0 to 10 minutes",
    "about 10 to 60 minutes",
    "about 1 to 3 hours",
    "about 3 to 6 hours",
    "more than 6 hours"
)
