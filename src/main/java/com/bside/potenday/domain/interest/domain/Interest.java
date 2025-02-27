package com.bside.potenday.domain.interest.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "interest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Access(AccessType.FIELD)
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long interestId;
    @Column(name = "interest_name")
    private String interestName;
}
