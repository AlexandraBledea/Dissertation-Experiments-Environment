package ubb.dissertation.rabbitmq_consumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ubb.dissertation.common.Message;
import ubb.dissertation.common.OshiLogger;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RabbitMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMessageConsumer.class);

    private final OshiLogger oshiLogger;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public RabbitMessageConsumer() throws IOException {
        this.oshiLogger = new OshiLogger("results.csv");
        scheduler.scheduleAtFixedRate(oshiLogger::log, 0, 1, TimeUnit.SECONDS);
    }

    @RabbitListener(queues = "${rabbit.queue-name}")
    public void receiveMessage(Message message) {
        long now = System.currentTimeMillis();
        long latency = now - message.getTimestamp();
        oshiLogger.recordMessage(latency);

        log.info("RabbitMQ: Received message {} of {}, latency: {} ms", message.getMessageNumber(), message.getNumberOfMessages(), latency);
    }
}
