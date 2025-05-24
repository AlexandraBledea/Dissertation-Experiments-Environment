package ubb.dissertation.producer.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ubb.dissertation.common.Message;

@Component
@Slf4j
public class RedisClient implements MessageBrokerClient{

    RedisTemplate<String, Message> redisTemplate;
    private final String channel;

    public RedisClient(RedisTemplate<String, Message> redisTemplate, @Value("${redis-channel}")String channel) {
        this.redisTemplate = redisTemplate;
        this.channel = channel;
    }

    @Override
    public void sendMessage(Message msg) {
        redisTemplate.convertAndSend(channel, msg);
    }
}
