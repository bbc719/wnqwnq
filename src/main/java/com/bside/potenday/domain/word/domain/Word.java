package com.bside.potenday.domain.word.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "word")
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Getter
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id", nullable = false)
    private Long wordId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "user_interest_id", nullable = false)
    private Long userInterestId;
    @Column(name = "word", nullable = false)
    private String word;
    @Column(name = "meaning", nullable = false)
    private String meaning;
    @Column(name = "pos")
    private String pos;
    @Column(name = "ex", nullable = false)
    private String ex;
    @Column(name = "tr", nullable = false)
    private String tr;

    public Word(Long userId, Long userInterestId, String word, String meaning, String pos, String ex, String tr) {
        this.userId = userId;
        this.userInterestId = userInterestId;
        this.word = word;
        this.meaning = meaning;
        this.pos = pos;
        this.ex = ex;
        this.tr = tr;
    }
}
