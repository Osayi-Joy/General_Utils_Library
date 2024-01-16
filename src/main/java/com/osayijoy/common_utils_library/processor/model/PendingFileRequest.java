package com.osayijoy.common_utils_library.processor.model;


import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;



@Entity
@Table(name = "pending_file_request")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PendingFileRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String documentType;

    private String pathToFile;

    @ManyToOne
    @JoinColumn(name = "approval_requests_id")
    private ApprovalRequests approvalRequests;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PendingFileRequest that = (PendingFileRequest) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
