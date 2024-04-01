package com.example.screenmanagement.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

public class DateUtils {
    public static Date convertStringToDate(String dateString) throws ParseException {
        if (dateString == null) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(dateString);
    }

    public static int getNumberOfDaysInMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }

    public static boolean isWeekday(DayOfWeek dayOfWeek) {
        return !dayOfWeek.equals(DayOfWeek.SATURDAY)  && !dayOfWeek.equals(DayOfWeek.SUNDAY);
    }


    public static DayOfWeek getDayOfWeek(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return date.getDayOfWeek();
    }

    public static String mapDayOfWeekToString(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> "Mon";
            case 2 -> "Tue";
            case 3 -> "Wed";
            case 4 -> "Thu";
            case 5 -> "Fri";
            case 6 -> "Sat";
            case 7 -> "Sun";
            default -> "";
        };
    }

    public static boolean isSameCurrentMonthAndYear(int month, int year) {
        int currentDay = LocalDate.now().getDayOfMonth();
        LocalDate currentDate = LocalDate.now();

        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        return month == currentMonth && year == currentYear;
    }

}
