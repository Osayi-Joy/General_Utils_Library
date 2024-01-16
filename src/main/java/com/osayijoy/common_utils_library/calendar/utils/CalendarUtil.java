package com.osayijoy.common_utils_library.calendar.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CalendarUtil {

    public static void checkAppointmentStartAndEndTimeIsValid(LocalTime endTime, LocalTime startTime, LocalDate date) throws RuntimeException {
        if(LocalTime.now().isAfter(startTime) && LocalDate.now().equals(date)){
            throw new RuntimeException("You can only schedule meeting start time in the future");
        }

        if(LocalTime.now().isAfter(endTime) && LocalDate.now().equals(date)){
            throw new RuntimeException("You can only schedule meeting end time in the future");
        }

        if(startTime.isAfter(endTime)){
            throw new RuntimeException("Your start time can't come after end time");
        }

        if(endTime.isBefore(startTime)){
            throw new RuntimeException("Your end time can't come after start time");
        }

        if(endTime.equals(startTime)){
            throw new RuntimeException("Your meeting start and end time are the same");
        }
    }

    public static void checkAppointmentTimeIsValid(LocalTime endTime, LocalTime startTime) throws RuntimeException {
        if(startTime.isAfter(LocalTime.of(20, 1))
                || endTime.isAfter(LocalTime.of(20, 1))){
            throw new RuntimeException("You can't set an appointment past 8PM");
        }

        if(startTime.isBefore(LocalTime.of(8, 0))
                || endTime.isBefore(LocalTime.of(8, 0))){
            throw new RuntimeException("You can't set an appointment before 8AM");
        }
    }

    public static void checkAppointmentDateIsValid(LocalDate appointmentDate) throws RuntimeException {
        if(appointmentDate.isBefore(LocalDate.now())){
            throw new RuntimeException("You can only schedule for future dates");
        }
    }
}
