## Here will be more info about this project

### List of services:
- *Transaction service*

- *Customer service*



```

  transaction-service:
    build:
      context: ./TransactionService
      dockerfile: Dockerfile   
    container_name: transaction-service
    ports:
      - "8081:8080" 
    depends_on:
      - rabbitmq
      - oracle-db
    environment:
      SPRING_DATASOURCE_URL: jdbc:oracle:thin:@oracle-db:1521/FREEPDB1
      SPRING_DATASOURCE_USERNAME: system
      SPRING_DATASOURCE_PASSWORD: 1234
      SPRING_RABBITMQ_HOST: rabbitmq
      SPRING_RABBITMQ_USERNAME: myuser
      SPRING_RABBITMQ_PASSWORD: mypassword

  customer-service:
    build:
      context: ./CustomerService
      dockerfile: Dockerfile
    container_name: customer-service
    ports:
      - "8082:8080"  
    depends_on:
      - rabbitmq
      - oracle-db
    environment:
      SPRING_DATASOURCE_URL: jdbc:oracle:thin:@oracle-db:1521/FREEPDB1
      SPRING_DATASOURCE_USERNAME: system
      SPRING_DATASOURCE_PASSWORD: 1234
      SPRING_RABBITMQ_HOST: rabbitmq
      SPRING_RABBITMQ_USERNAME: myuser
      SPRING_RABBITMQ_PASSWORD: mypassword
```