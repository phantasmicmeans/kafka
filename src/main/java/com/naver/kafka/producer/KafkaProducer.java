package com.naver.kafka.producer;

import com.naver.kafka.data.Greeting;
import com.naver.kafka.data.MetaData;
import com.naver.kafka.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, Greeting> kafkaJsonTemplate;

    public Mono<MetaData> sendMessage(String msg) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("sangmin", msg);
        return Mono.fromFuture(future.completable())
                .map(SendResult::getRecordMetadata)
                .map(this::convert)
                .doOnError(JsonUtils::print);
    }

    public Mono<MetaData> sendJsonMessage(Greeting msg) {
        ListenableFuture<SendResult<String, Greeting>> future = kafkaJsonTemplate.send("greeting", msg);
        return Mono.fromFuture(future.completable())
                .map(SendResult::getRecordMetadata)
                .map(this::convert)
                .doOnError(JsonUtils::print);
    }

    private MetaData convert(RecordMetadata recordMetadata) {
        return MetaData.builder()
                .offset(recordMetadata.offset())
                .partition(recordMetadata.partition())
                .topic(recordMetadata.topic())
                .timestamp(recordMetadata.timestamp())
                .serializedKeySize(recordMetadata.serializedKeySize())
                .serializedValueSize(recordMetadata.serializedValueSize())
                .build();
    }
}
