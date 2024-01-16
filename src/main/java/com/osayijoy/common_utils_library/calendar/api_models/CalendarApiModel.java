package com.osayijoy.common_utils_library.calendar.api_models;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalTime;

import com.osayijoy.common_utils_library.calendar.enums.CalendarStatus;
import com.osayijoy.common_utils_library.calendar.enums.CalendarType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarApiModel {
    private Long appointmentId;
    private String appointmentTitle;
    private LocalDate appointmentDate;
    private LocalTime appointmentStartTime;
    private LocalTime appointmentEndTime;
    private String appointmentNote;
    private String host;
    private String attendee;
    private CalendarType calendarType;
    private CalendarStatus calendarStatus;
    private String appointmentPlace;


}
