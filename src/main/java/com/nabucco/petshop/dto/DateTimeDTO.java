package com.nabucco.petshop.dto;

import lombok.Getter;

@Getter
public class DateTimeDTO {

  private String year;
  private String week;
  private String day;
  private String minute;

  public DateTimeDTO(String year, String week, String day, String minute) {
    this.year = year;
    this.week = week;
    this.day = day;
    this.minute = minute;
  }

  public Integer getYearAsIntteger() {
    return convertStringToInteger(year);
  }

  public Integer getWeekAsIntteger() {
    return convertStringToInteger(week);
  }

  public Integer getDayAsIntteger() {
    return convertStringToInteger(day);
  }

  public Integer getMinuteAsIntteger() {
    return convertStringToInteger(minute);
  }

  private Integer convertStringToInteger(String number) {
    try {
      return Integer.valueOf(number);
    } catch (Exception e) {
      return null;
    }
  }
}
