package ubb.dissertation.redis_consumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ubb.dissertation.common.Message;
import ubb.dissertation.common.OshiLogger;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RedisMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(RedisMessageConsumer.class);

    private final OshiLogger oshiLogger;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public RedisMessageConsumer() throws IOException {
        this.oshiLogger = new OshiLogger("results.csv");
        scheduler.scheduleAtFixedRate(oshiLogger::log, 0, 1, TimeUnit.SECONDS);
    }

    public void onMessage(Message message) {
        long now = System.currentTimeMillis();
        long latency = now - message.getTimestamp();
        oshiLogger.recordMessage(latency);

        log.info("Redis: Received message {} of {}, latency: {} ms", message.getMessageNumber(), message.getNumberOfMessages(), latency);
    }
}
