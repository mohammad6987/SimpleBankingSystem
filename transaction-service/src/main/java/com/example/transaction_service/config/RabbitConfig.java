package com.example.transaction_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ACCOUNT_REQUEST_QUEUE = "account.request";
    public static final String ACCOUNT_RESPONSE_QUEUE = "account.response";

    @Bean
    public Queue accountRequestQueue() {
        return new Queue(ACCOUNT_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue accountResponseQueue() {
        return new Queue(ACCOUNT_RESPONSE_QUEUE, true);
    }
/* 
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter());
        return template;
    }*/
}
