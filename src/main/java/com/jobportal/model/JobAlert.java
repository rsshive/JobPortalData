package com.jobportal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "job_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alertId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;

    @Column(nullable = false, length = 200)
    private String keywords; // Match against vacancy title/skills

    @Column(length = 255)
    private String location; // Match against vacancy location

    @Column(name = "salary_min")
    private Integer salaryMin; // Match against vacancy salaryMax/Min

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
}
