package com.bside.potenday.domain.feed.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "word")
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Getter
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_key", nullable = false)
    private Long topicId;
    @Column(name = "user_interest_id", nullable = false)
    private Long userInterestId;
    @Column(name = "word", nullable = false)
    private String word;
    @Column(name = "word_meaning", nullable = false)
    private String wordMeaning;
    @Column(name = "pos")
    private String pos;
    @Column(name = "ex_sentence", nullable = false)
    private String exSentence;
    @Column(name = "ex_translate", nullable = false)
    private String exTranslate;
    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "clicked_at", nullable = false)
    private LocalDateTime clickedAt;

    public Word(Long userInterestId, String word, String wordMeaning, String pos, String exSentence, String exTranslate) {
        this.userInterestId = userInterestId;
        this.word = word;
        this.wordMeaning = wordMeaning;
        this.pos = pos;
        this.exSentence = exSentence;
        this.exTranslate = exTranslate;
        this.clickedAt = LocalDateTime.now();
    }
}
