package com.osayijoy.common_utils_library.calendar.services;

import java.time.LocalDate;

import com.osayijoy.common_utils_library.calendar.dto.CalendarDTO;
import org.springframework.data.domain.Page;

public interface BaseCalendarService<C> {

    default C createAppointment(CalendarDTO calendarDTO) throws RuntimeException{
        throw new RuntimeException("No implementation written to create a calendar appointment");
    }

    default Page<C> fetchAllAppointmentsByUserAndDate(Long userId, LocalDate date, int page, int size) throws RuntimeException {
        throw new RuntimeException("No implementation written to fetch all calendar appointments by user and date");
    }

    default Page<C> fetchAllAppointmentsByUser(Long userId, int page, int size) throws RuntimeException {
        throw new RuntimeException("No implementation written to fetch all calendar appointments by user");
    }

    default C fetchSingleAppointment(Long appointmentId) throws RuntimeException {
        throw new RuntimeException("No implementation written to fetch a calendar appointment by id");
    }

    default Page<C> fetchAllAppointments(int page, int size) throws RuntimeException {
        throw new RuntimeException("No implementation written to fetch all calendar appointments");
    }

    default C cancelAppointment(Long appointmentId, String note) throws RuntimeException {
        throw new RuntimeException("No implementation written to create a calendar appointment");
    }

    default C updateToArrived(Long appointmentId, String note) throws RuntimeException {
        throw new RuntimeException("No implementation written to cancel an appointment");
    }

    default C updateToCompleted(Long appointmentId, String note) throws RuntimeException {
        throw new RuntimeException("No implementation written to update an appointment as completed");
    }

}
