package ubb.dissertation.rabbitmq_consumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ubb.dissertation.common.Message;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RabbitMessageConsumer {

    private int received = 0;
    private long start = 0;
    private final List<Long> latencies = new ArrayList<>();

    @RabbitListener(queues = "${rabbit.queue-name}")
    public void receiveMessage(Message message) {
        long now = System.currentTimeMillis();
        long latency = now - message.getTimestamp();
        latencies.add(latency);

        if (received == 0) {
            start = now;
        }

        received++;
        log.info("RabbitMQ: Received message {} of {}, latency: {} ms", message.getMessageNumber(), message.getNumberOfMessages(), latency);

        if (received == message.getNumberOfMessages()) {
            long duration = now - start;
            double throughput = duration > 0 ? (double) received / (duration / 1000.0) : 0;
            double avgLatency = latencies.stream().mapToLong(Long::longValue).average().orElse(0);

            log.info("RabbitMQ Batch Complete - Latency: avg ~{}ms, Throughput: {}msg/sec",
                    avgLatency, throughput);

            received = 0;
            latencies.clear();
        }
    }
}
