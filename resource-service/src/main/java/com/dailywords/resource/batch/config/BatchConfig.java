package com.dailywords.resource.batch.config;

import com.dailywords.resource.batch.listener.RandomWordItemReadListener;
import com.dailywords.resource.batch.processor.FilterWordsItemProcessor;
import com.dailywords.resource.batch.processor.WordItemProcessor;
import com.dailywords.resource.domain.RandomWordData;
import com.dailywords.resource.domain.entity.Word;
import com.dailywords.resource.service.RandomWordDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@EnableBatchProcessing
@Configuration
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    @Value("${spring.kafka.template.default-topic}")
    private String randomWordsTopic;
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.consumer.group-id}")
    private String randomWordsGroupId;

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
    }

    private Properties kafkaConsumerProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RandomWordDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, randomWordsGroupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return props;
    }

    @Bean
    public KafkaItemReader<String, RandomWordData> kafkaItemReader() {
        return new KafkaItemReaderBuilder<String, RandomWordData>()
                .partitions(0)
                .consumerProperties(kafkaConsumerProperties())
                .name("kafkaItemReader")
                .topic(randomWordsTopic)
                .build();
    }
//
//    @Bean
//    public ItemWriter<Word> jdbcItemWriter(final DataSource dataSource) {
//        return new JdbcBatchItemWriterBuilder<Word>()
//                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//                .sql("")
//                .dataSource(dataSource)
//                .build();
//    }

    @Bean
    public JpaItemWriter<Word> jpaItemWriter() {
        return new JpaItemWriterBuilder<Word>()
                .usePersist(true)
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public ItemProcessor<RandomWordData, Word> wordItemProcessor() {
        return new WordItemProcessor();
    }

    @Bean
    public ItemProcessor<RandomWordData, RandomWordData> filterWordsItemProcessor() {
        return new FilterWordsItemProcessor();
    }

    @Bean
    public ItemProcessor<RandomWordData, Word> compositeWordItemProcessor() {
        return new CompositeItemProcessorBuilder<RandomWordData, Word>()
                .delegates(filterWordsItemProcessor(), wordItemProcessor())
                .build();
    }

    @Bean
    public ItemReadListener<RandomWordData> randomWordDataItemReadListener() {
        return new RandomWordItemReadListener();
    }

    @Bean
    public Step fetchRandomWords() {
        return stepBuilderFactory.get("getRandomWordsStep")
                .<RandomWordData, Word>chunk(20)
                .reader(kafkaItemReader())
                .listener(randomWordDataItemReadListener())
                .processor(compositeWordItemProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public Job getRandomWordsJob() {
        return jobBuilderFactory.get("getRandomWords")
                .incrementer(new RunIdIncrementer())
                .start(fetchRandomWords())
                .build();
    }
}
