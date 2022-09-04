package com.dailywords.resource.repository;

import com.dailywords.resource.domain.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findFirstByWord(String word);
    List<Word> findByWord(String word);
}
