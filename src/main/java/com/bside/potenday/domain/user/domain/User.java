package com.bside.potenday.domain.user.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

//https://wooj-coding-fordeveloper.tistory.com/76 참고
@Entity
@Table(name = "user")
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "username", nullable = false)
    private String username;
    @Embedded
    private UserOauth userOauth;
    @Enumerated(EnumType.STRING)
    @Column(name = "job")
    private Job job;
    @Column(name = "profile_img")
    private String profileImg;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "push_notification_agree")
    private Boolean pushNotificationAgree;
    @Column(name = "marketing_info_agree")
    private Boolean marketingInfoAgree;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User(String username, String email, String profileImg, UserOauth userOauth) {
        this.username = username;
        this.email = email;
        this.profileImg = profileImg;
        this.userOauth = userOauth;
        this.marketingInfoAgree = false;
        this.pushNotificationAgree = false;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAgree(boolean agree) {
        this.pushNotificationAgree = agree;
        this.marketingInfoAgree = agree;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateNickName(String nickname) {
        this.nickname = nickname;
        this.job = Job.WORKER;
        this.updatedAt = LocalDateTime.now();
    }
}
