package ubb.dissertation.producer.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ubb.dissertation.common.Message;

@Component
@Slf4j
public class KafkaClient implements MessageBrokerClient{

    KafkaTemplate<String, Message> kafkaTemplate;
    private final String topic;

    public KafkaClient(KafkaTemplate<String, Message> kafkaTemplate, @Value("${kafka-topic}")String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void sendMessage(Message message) {
        kafkaTemplate.send(topic, message);
    }
}
