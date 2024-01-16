package com.osayijoy.common_utils_library.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.osayijoy.common_utils_library.calendar.enums.CalendarType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalendarDTO {
    private LocalDate appointmentDate;
    private LocalTime appointmentStartTime;
    private LocalTime appointmentEndTime;
    private String appointmentNote;
    private String attendee;
    private CalendarType calendarType;
    private String appointmentPlace;
    private String host;
    private String appointmentTitle;
    private String hostFirstName;
    private String hostLastName;
    private String guestFirstName;
    private String guestLastName;
}
