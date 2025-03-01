package com.bside.potenday.domain.word.repository;

import com.bside.potenday.domain.word.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
}
