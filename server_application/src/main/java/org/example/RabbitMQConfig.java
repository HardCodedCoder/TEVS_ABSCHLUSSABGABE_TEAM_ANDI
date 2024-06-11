package org.example;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${server.node-id}")
    private String nodeId;

    @Bean
    public FanoutExchange statusExchange() {
        return new FanoutExchange("statusExchange");
    }

    @Bean
    public Queue statusQueue() {
        return new Queue("statusQueue-" + nodeId);
    }

    @Bean
    public Binding bindingStatusQueue(Queue statusQueue, FanoutExchange statusExchange) {
        return BindingBuilder.bind(statusQueue).to(statusExchange);
    }
}
