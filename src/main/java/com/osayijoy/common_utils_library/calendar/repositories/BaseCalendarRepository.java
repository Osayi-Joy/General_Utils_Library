package com.osayijoy.common_utils_library.calendar.repositories;

import java.time.LocalDate;
import java.util.Optional;

import com.osayijoy.common_utils_library.calendar.models.BaseCalendarModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseCalendarRepository<C extends BaseCalendarModel> extends JpaRepository<C, Long> {

    Page<C> findAllAppointmentsByAppointmentDate(LocalDate date, Pageable pageable);

    Page<C> findAllAppointments(String userId, Pageable pageable);

    Optional<C> findSingleAppointment(Long id);

    Page<C> findAllAppointmentsByAppointmentDateAndUser(String userId, LocalDate date, Pageable pageable);
}
