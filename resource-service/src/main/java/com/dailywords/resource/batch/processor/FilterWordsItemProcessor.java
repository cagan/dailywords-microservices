package com.dailywords.resource.batch.processor;

import com.dailywords.resource.domain.RandomWordData;
import com.dailywords.resource.domain.entity.Word;
import com.dailywords.resource.repository.WordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FilterWordsItemProcessor implements ItemProcessor<RandomWordData, RandomWordData> {
    private static final Logger log = LoggerFactory.getLogger(FilterWordsItemProcessor.class);

    @Autowired
    private WordRepository wordRepository;

    @Override
    public RandomWordData process(RandomWordData randomWordData) {
        List<Word> optionalWord = wordRepository.findByWord(randomWordData.getWord());

        if (optionalWord.isEmpty()) {
            return randomWordData;
        }

        log.info("[WORD: {}] is found. Skipping...", optionalWord.stream().findFirst().get().getWord());
        return null;
    }
}
