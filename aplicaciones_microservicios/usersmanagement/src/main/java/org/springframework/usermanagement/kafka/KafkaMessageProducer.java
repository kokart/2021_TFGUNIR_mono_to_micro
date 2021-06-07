/*
 * Clase para mandar mensajes a Kafka
 */
package org.springframework.usermanagement.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class KafkaMessageProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private String application1 = "Editrans";

	private String application2 = "Ifiweb";

	@Value(value = "${message.topic.name}")
	private String topicName;

	@Value(value = "${message.topic.name2}")
	private String topicName2;

	public void sendMessage(String topic, String message) {
		// if (topic==null || topic.trim().equals(""))
		// Asignamos topic en función de la aplicación a la que pertenece el usuario
		if (topic.equals(application1))
			topic = topicName;
		else if (topic.equals(application2))
			topic = topicName2;

		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);

		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			@Override
			public void onSuccess(SendResult<String, String> result) {
				System.out.println(
						"Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
			}

			@Override
			public void onFailure(Throwable ex) {
				System.err.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
			}
		});
	}

}
