package ubb.dissertation.kafka_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ubb.dissertation.common.Message;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class KafkaMessageConsumer {

    private int received = 0;
    private long start = 0;
    private final List<Long> latencies = new ArrayList<>();


    @KafkaListener(topics = "${kafka-topic}")
    public void consume(Message message) throws JsonProcessingException {
        long now = System.currentTimeMillis();
        long latency = System.currentTimeMillis() - message.getTimestamp();
        latencies.add(latency);

        if (received == 0) {
            start = now;
        }

        received++;
        log.info("Kafka: Received message {} of {}, latency: {} ms", message.getMessageNumber(), message.getNumberOfMessages(), latency);

        //when done, print summary
        if (received == message.getNumberOfMessages()) {
            long duration = now - start;
            double throughput = (double) received / (duration / 1000.0);
            double avgLatency = latencies.stream().mapToLong(Long::longValue).average().orElse(0);

            log.info("Kafka Batch Complete - Latency: avg ~{}ms, Throughput: {}msg/sec",
                    avgLatency, throughput);

            //Reset for next batch
            received = 0;
            latencies.clear();
        }

    }
}
