package com.dailywords.resource.batch.listener;

import com.dailywords.resource.domain.RandomWordData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class RandomWordItemReadListener implements ItemReadListener<RandomWordData> {
    private static final Logger log = LoggerFactory.getLogger(RandomWordItemReadListener.class);

    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(RandomWordData randomWordData) {
        log.info("ITEM [RandomWordData: {}] has been red", randomWordData);
    }

    @Override
    public void onReadError(Exception e) {
    }
}
