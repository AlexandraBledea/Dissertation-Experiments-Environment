package ubb.dissertation.producer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ubb.dissertation.common.Message;
import ubb.dissertation.producer.client.MessageBrokerClient;

@Service
@Slf4j
public class ProducerService {

    public void sendBatch(int count, int sizeKB, MessageBrokerClient client) {
        long start = System.currentTimeMillis();

        for(int i = 0; i < count; i++) {
            Message message = new Message(i + 1, count, sizeKB);
            client.sendMessage(message);
            log.info("Sent message {} of {}", i + 1, count);
        }

        long end = System.currentTimeMillis();
        log.info("Sent {} message of {}KB in {} ms", count, sizeKB, end - start);
    }
}
