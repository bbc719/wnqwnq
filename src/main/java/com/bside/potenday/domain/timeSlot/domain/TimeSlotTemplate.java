package com.bside.potenday.domain.timeSlot.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "time_slot_template")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class TimeSlotTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    @JsonIgnore
    private Long templateId;
    @Column(name = "user_id", nullable = false)
    @JsonIgnore
    private Long userId;
    @Column(name = "template_name", nullable = false)
    private String templateName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Column(name = "start_time")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Column(name = "end_time")
    private LocalTime endTime;
    @Column(name = "updated_at")
    @JsonIgnore
    private LocalDateTime updatedAt;

    public TimeSlotTemplate(Long userId, String templateName, LocalTime startTime, LocalTime endTime) {
        this.userId = userId;
        this.templateName = templateName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeSlotTemplate(Long templateId, Long userId, String templateName, LocalTime startTime, LocalTime endTime) {
        this.templateId = templateId;
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

    public void updateSlot(String templateName, LocalTime startTime, LocalTime endTime) {
        this.templateName = templateName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
