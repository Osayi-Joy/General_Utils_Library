package com.osayijoy.common_utils_library.processor.model;


import com.osayijoy.common_utils_library.processor.enums.ApprovalRequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "approval_requests")
public class ApprovalRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String description;
    @Column(columnDefinition = "text")
    private String dataToUpdate;
    @Column(columnDefinition = "text")
    private String initialData;
    @NotNull
    @NotEmpty
    private String requesterUsername;
    private String approvalUsername;
    private boolean approved;
    @Enumerated(EnumType.STRING)
    private ApprovalRequestStatus status;
    private String approvalRequestType;
    private String permission;
    private LocalDateTime createdOn;
    private LocalDateTime treatedDate;
    private String errorTrace;


    @OneToMany(
            mappedBy = "approvalRequests",
            fetch = FetchType.EAGER
    )
    private Collection<PendingFileRequest> pendingFileRequests = new ArrayList<>();





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApprovalRequests that = (ApprovalRequests) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
