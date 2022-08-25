package com.example.intermediate.dto.response;



import com.example.intermediate.domain.Time;

import java.util.Date;

public class TimeDto {

    private String date; // String으로 선언해 줄 것 - "1 일전", "12 시간 전" ..

    public String getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = Time.calculateTime(date); // 기존의 getter, setter에서 변경된 부분
    }
}
