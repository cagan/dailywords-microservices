package com.dailywords.resource.domain;

import com.dailywords.resource.domain.entity.Word;
import lombok.extern.log4j.Log4j2;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;

import java.util.Map;

@Log4j2
public class HibernateEventListener implements PersistEventListener {

    @Override
    public void onPersist(PersistEvent persistEvent) throws HibernateException {
        Object entity = persistEvent.getObject();
        if (entity instanceof Word) {
            Word word = (Word) entity;
            log.info("[WORD: {}] Item has been saved to the database", word.getWord());
        }

    }

    @Override
    public void onPersist(PersistEvent persistEvent, Map map) throws HibernateException {
        Object entity = persistEvent.getObject();
        if (entity instanceof Word) {
            Word word = (Word) entity;
            //TODO handle logic...
        }

    }
}