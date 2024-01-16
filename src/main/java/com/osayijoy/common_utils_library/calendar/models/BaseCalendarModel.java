package com.osayijoy.common_utils_library.calendar.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.calendar.enums.CalendarStatus;
import com.osayijoy.common_utils_library.calendar.enums.CalendarType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Getter
@Setter
public class BaseCalendarModel {

    @Column(name = "appointment_title", nullable = false)
    private String appointmentTitle;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_start_time", nullable = false)
    private LocalTime appointmentStartTime;

    @Column(name = "appointment_end_time", nullable = false)
    private LocalTime appointmentEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status", nullable = false)
    private CalendarStatus calendarStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false)
    private CalendarType calendarType;

    @Column(name = "location", nullable = false)
    private String appointmentPlace;

    @Column(name = "appointment_note")
    private String appointmentNote;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_modified")
    private LocalDateTime dateModified;
}
