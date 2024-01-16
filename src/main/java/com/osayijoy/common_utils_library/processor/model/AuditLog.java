package com.osayijoy.common_utils_library.processor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;



@Entity
@Table(name = "audit_log")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String email;

    private String role;

    private String activity;

    private String activityDescription;

    private boolean activitySuccessfullyDone;

    private LocalDateTime logStartDate;

    private LocalDateTime logEndDate;

    private String ipAddress;

    private String auditType = "N/A";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AuditLog auditLog = (AuditLog) o;
        return id != null && Objects.equals(id, auditLog.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
