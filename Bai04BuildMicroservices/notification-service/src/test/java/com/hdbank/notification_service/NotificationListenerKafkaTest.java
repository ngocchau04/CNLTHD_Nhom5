package com.hdbank.notification_service;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import com.hdbank.notification_service.dto.OrderNotification;
import com.hdbank.notification_service.service.NotificationService;

@SpringBootTest(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.group-id=test-group"
})
@EmbeddedKafka(partitions = 1, topics = "notificationTopic")
class NotificationListenerKafkaTest {

    @Autowired
    private EmbeddedKafkaBroker broker;

    @SpyBean
    private NotificationService notificationService;

    @Test
    void kafkaConsumer_shouldReceiveMessage() {
        Map<String, Object> props = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker.getBrokersAsString(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );

        KafkaTemplate<String, OrderNotification> template =
                new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));

        template.send("notificationTopic", new OrderNotification("ORD123", "Order Placed Successfully"));
        template.flush();

        verify(notificationService, timeout(5000)).handleNotification(
                new OrderNotification("ORD123", "Order Placed Successfully")
        );
    }
}
