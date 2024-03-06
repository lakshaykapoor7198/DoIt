package com.app.doit.model;

import java.util.Objects;

public class TodoItemDto {
    private Integer hour;
    private Integer minute;
    private String title;
    private Integer day;
    private Integer month;
    private Integer year;
    private int alarmRequestId;

    public TodoItemDto() {}

    public boolean validate(){
        return !(title == null || title.isEmpty() || minute == null || hour == null);
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public int getAlarmRequestId() {
        return alarmRequestId;
    }

    public void setAlarmRequestId(int alarmRequestId) {
        this.alarmRequestId = alarmRequestId;
    }

    @Override
    public String toString() {
        return "TodoItemDto{" +
                "hour=" + hour +
                ", minute=" + minute +
                ", title='" + title + '\'' +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", alarmRequestId=" + alarmRequestId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItemDto that = (TodoItemDto) o;
        return alarmRequestId == that.alarmRequestId && Objects.equals(hour, that.hour) && Objects.equals(minute, that.minute) && Objects.equals(title, that.title) && Objects.equals(day, that.day) && Objects.equals(month, that.month) && Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour, minute, title, day, month, year, alarmRequestId);
    }
}
