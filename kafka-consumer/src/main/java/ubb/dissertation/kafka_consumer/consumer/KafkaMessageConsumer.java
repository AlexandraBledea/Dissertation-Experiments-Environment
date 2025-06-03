package ubb.dissertation.kafka_consumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ubb.dissertation.common.Message;
import ubb.dissertation.common.OshiLogger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

@Component
public class KafkaMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    private final OshiLogger oshiLogger;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public KafkaMessageConsumer() throws IOException {
        this.oshiLogger = new OshiLogger("results.csv");
        scheduler.scheduleAtFixedRate(oshiLogger::log, 0, 1, TimeUnit.SECONDS);
    }

    @KafkaListener(topics = "${kafka-topic}")
    public void consume(List<Message> messages, Acknowledgment ack) {
        long now = System.currentTimeMillis();

        if (messages.isEmpty()) return;

        for (Message message : messages) {
            long latency = now - message.getTimestamp();
            oshiLogger.recordMessage(latency);

            log.info("Kafka: Received message {} of {}, latency: {} ms",
                    message.getMessageNumber(), message.getNumberOfMessages(), latency);
        }
        ack.acknowledge();
    }

}
