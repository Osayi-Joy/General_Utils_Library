package com.osayijoy.common_utils_library.soteria.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osayijoy.common_utils_library.registhentication.registration.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel extends Auditable<String> implements Serializable {

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;



}
