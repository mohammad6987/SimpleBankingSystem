package Banking.TransactionService.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String ACCOUNT_CREATION_QUEUE = "account.creation.queue";
    public static final String TRANSACTION_QUEUE = "transaction.queue";
    
    @Bean
    public Queue accountCreationQueue() {
        return new Queue(ACCOUNT_CREATION_QUEUE, true); 
    }
    
    @Bean
    public Queue transactionQueue() {
        return new Queue(TRANSACTION_QUEUE, true); 
    }
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}