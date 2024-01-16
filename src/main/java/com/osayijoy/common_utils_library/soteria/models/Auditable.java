package com.osayijoy.common_utils_library.soteria.models;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;



@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @CreatedBy
    protected T createdBy;


    @CreatedDate
    protected LocalDateTime createdDate;

    @LastModifiedBy
    protected T lastModifiedBy;


    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;
}
