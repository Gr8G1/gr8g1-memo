package Gr8G1.prac.pojo.grammar;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PrDate {
  public static void main(String[] args) {
    // Java 시간 관련 모듈 변화 흐름
    // java.util.Date > java.util.Calendar > java.time(org.joda.time)

    // 로컬 컴퓨터의 현재 날짜 정보를 저장한 LocalDate 객체를 리턴
    LocalDate currentDate = LocalDate.now();
    // result : 2019-11-13

    // 파라미터로 주어진 날짜 정보를 저장한 LocalDate 객체를 리턴
    LocalDate targetDate = LocalDate.of(2019,11,12);
    //결과 : 2019-11-12

    // 로컬 컴퓨터의 현재 시간 정보를 저장한 LocalDate 객체를 리턴.
    LocalTime currentTime = LocalTime.now();
    // 결과 : 18:34:22

    // 파라미터로 주어진 시간 정보를 저장한 LocalTime 객체를 리턴.
    LocalTime targetTime = LocalTime.of(12,33,35,22);
    // 결과 : 12:32:33.0000022

    // 로컬 컴퓨터의 현재 날짜와 시간 정보
    LocalDateTime currentDateTime = LocalDateTime.now();
    // 결과 : 2019-11-12T16:34:30.388
    LocalDateTime targetDateTime = LocalDateTime.of(2019, 11, 12, 12, 32,22,3333);
    // 결과 : 2019-11-12T12:32:22.000003333

    // 날짜 형식 변경
    DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    System.out.println("dateTimeFormatter = " + now.format(dateTimeFormatter1));
    System.out.println("dateTimeFormatter = " + now.format(dateTimeFormatter2));

    // 날짜 변환
    // LocalDate -> String
    LocalDate.of(2020, 12, 12).format(DateTimeFormatter.BASIC_ISO_DATE);

    // LocalDateTime -> String
    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // LocalDate -> java.sql.Date
    Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

    // LocalDateTime -> java.sql.Timestamp
    Timestamp.valueOf(LocalDateTime.now());

    // String -> LocalDate
    LocalDate.parse("1995-05-09");
    LocalDate parse = LocalDate.parse("20191224", DateTimeFormatter.BASIC_ISO_DATE);
    System.out.println("parse = " + parse);


    // String -> LocalDateTime
    LocalDateTime.parse("2019-12-25T10:15:30");
    LocalDateTime.parse("2019-12-25 12:30:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // java.util.Date -> LocalDateTime
    LocalDateTime dateTime1 = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
    System.out.println("dateTime1 = " + dateTime1);

    // LocalDateTime -> LocalDate
    LocalDate.from(LocalDateTime.now());

    // LocalDate -> LocalDateTime
    LocalDate.now().atTime(2, 30);

    LocalDateTime dateTime = now.toLocalDate().atStartOfDay();
    // MIDNIGHT (00:00), MIN (00:00), NOON (12:00), and MAX(23:59:59.999999999).

    LocalDateTime startOfDay = LocalDateTime.of(now.toLocalDate().minusDays(1), LocalTime.MIN);
    LocalDateTime endOfDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);

    System.out.println("startOfDay = " + startOfDay);
    System.out.println("endOfDay = " + endOfDay);
  }
}
