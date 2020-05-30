package com.naver.kafka.api;

import com.naver.kafka.data.Greeting;
import com.naver.kafka.data.MetaData;
import com.naver.kafka.producer.KafkaProducer;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class KafkaController {
    public final KafkaProducer kafkaProducer;

    @GetMapping("/text")
    public Mono<MetaData> test() {
        return kafkaProducer.sendMessage("test");
    }

    @GetMapping("/greeting")
    public Mono<MetaData> greeting() {
        Greeting greeting = new Greeting("messsage", "sangmin");
        return kafkaProducer.sendJsonMessage(greeting);
    }
}
