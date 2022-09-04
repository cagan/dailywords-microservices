package com.dailywords.resource.batch.processor;

import com.dailywords.resource.domain.RandomWordData;
import com.dailywords.resource.domain.entity.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class WordItemProcessor implements ItemProcessor<RandomWordData, Word> {
    private static final Logger log = LoggerFactory.getLogger(FilterWordsItemProcessor.class);

    @Override
    public Word process(RandomWordData data) {
        log.info("[PROCESSING WORD: {}]...", data.getWord());
        return data.getResults().stream()
                .map(result -> {
                    Word word = new Word();
                    word.setWord(data.getWord());
                    word.setDefinition(result.getDefinition());
                    word.setSynonyms(result.getSynonyms());
                    word.setTypeOf(result.getTypeOf());
                    word.setPartOfSpeech(result.getPartOfSpeech());
                    word.setResults(data.getResults());
                    return word;
                }).findFirst().orElse(null);
    }
}
