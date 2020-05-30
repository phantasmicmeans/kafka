package com.naver.kafka.consumer;

import com.naver.kafka.data.Greeting;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaConsumer {

    @KafkaListener(topicPartitions = @TopicPartition(topic = "sangmin", partitionOffsets = {
            @PartitionOffset(partition = "0", initialOffset = "0")}), groupId = "foo",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenToPartition(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("listenToPartition 0 " + message + ", " + partition);
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "sangmin", partitions = { "1", "2"}), groupId = "foo",
            containerFactory = "filterKafkaListenerContainerFactory")
    public void listenToPartitionWithFilter(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("listenToPartition 0, 1 with filter " + message + ", " + partition);
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "greeting", partitions = {"0", "1"}), groupId = "foo",
            containerFactory = "greetingKafkaListenerContainerFactory")
    public void listenToPartitionGreeting(@Payload Greeting greeting) {
        log.info("greetingKafkaListenerContainerFactory " + greeting.getMsg() + ", " + greeting.getName());
    }
}
