package com.bside.potenday.domain.timeSlot.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "time_slot_template")
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Getter
public class TimeSlotTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long templateId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "template_name", nullable = false)
    private String templateName;
    @Column(name="start_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @Column(name="end_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public TimeSlotTemplate(Long userId, String templateName, LocalTime startTime, LocalTime endTime) {
        this.userId = userId;
        this.templateName = templateName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
