/**
 * 
 */
package org.example;

import java.io.IOException;

/**
 * @author aliaalzubaidi
 *
 */

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublishSample {

	private static MqttConnectOptions connOpts = new MqttConnectOptions();

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		
		//assigning the QoS a name.
		Config Availability = new Config();
		Availability.setQoS("biggerToknowBetter");

		//an instance of the Fabric SDK for conducting Blockchain operations that interfaces with the installed smart contract.
		final FabricSDK bridge = new FabricSDK();

		bridge.createQoSreport(Availability.getQoS());

		
		MqttClient sampleClient;
		final String topic = "sampling";
		String content = "Message from Monitoring agent";
		final int qos = 2;
		final String broker = "tcp://localhost:1883";
		final String clientId = "Client: ";
		final MemoryPersistence persistence = new MemoryPersistence();

		int validMQTTrequestsCount = 0;

		for (int i = 1; i <= 10; i++) {
			try {
				sampleClient = new MqttClient(broker, clientId + i, persistence);
				connOpts.setCleanSession(true);
				connOpts.setConnectionTimeout(2);
				System.out.println("Connecting to broker: " + broker);
				sampleClient.connect(connOpts);
				System.out.println("Connected");
				System.out.println("Publishing message: " + content);
				final MqttMessage message = new MqttMessage(content.getBytes());
				message.setQos(qos);

			

				content = "Iteration: " + i;
				sampleClient.publish(topic, message);
				System.out.println("Message published: " + (i));
				sampleClient.disconnect();
				System.out.println("Disconnected");
				validMQTTrequestsCount++;
			} catch (final MqttException me) {
				System.out.println("reason " + me.getReasonCode());
				System.out.println("msg " + me.getMessage());
				System.out.println("loc " + me.getLocalizedMessage());
				System.out.println("cause " + me.getCause());
				System.out.println("excep " + me);
				//me.printStackTrace();

				bridge.reportIncident(Availability.getQoS(), validMQTTrequestsCount, me.getMessage());
			}

		}


		bridge.assessCompliance("month345634554", Availability.getQoS(), validMQTTrequestsCount, "0");
	}

}
