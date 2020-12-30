/**
 * 
 */
package org.example;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

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

		// an instance of the Fabric SDK for conducting Blockchain operations that
		// interfaces with the installed smart contract.
		final FabricSDK bridge = new FabricSDK();

		// assigning the QoS a name.
		Config QoSmetric = new Config();
		QoSmetric.setQoS("Availability1");
		bridge.createQoSreport(QoSmetric.getQoS());

		// duration in days
		final int Duration = 5;
		final int minMQTTResuests = 0;
		final int MaxMQTTRequests = 10;
		// MQTT client
		MqttClient sampleClient;
		// MQTT topic
		final String topic = "Emergency";
		String content = "Patiant in critical situation";
		final int qos = 2;
		final String broker = "tcp://localhost:1883";
		final String clientId = "Patient ";
		final String reportID = "monthlyReport" + System.nanoTime();
		final MemoryPersistence persistence = new MemoryPersistence();

		// count of current valid MQTT requests
		int validMQTTrequestsCount = 0;
		// count of current Failed MQTT requests
		int failedMQTTrequestsCount = 0;

		// for testing purposes, we count all fail and valid requesrs for entire
		// emulation run for comparision purposes
		// total valid count of MQTT
		int totalFail = 0;
		int totalValid = 0;

		for (int i = 1; i <= Duration; i++) {
			// number of MQTT requests per day
			int MQTTRequests = ThreadLocalRandom.current().nextInt(minMQTTResuests, MaxMQTTRequests);

			for (int j = 0; j <= MQTTRequests; j++) {
				try {
					sampleClient = new MqttClient(broker, clientId, persistence);
					connOpts.setCleanSession(true);
					connOpts.setConnectionTimeout(2);
					System.out.println("Connecting to broker: " + broker);
					sampleClient.connect(connOpts);
					System.out.println("Connected");
					final MqttMessage message = new MqttMessage(content.getBytes());
					message.setQos(qos);

					sampleClient.publish(topic, message);
					System.out.println("Message published: " + (i));
					sampleClient.disconnect();
					System.out.println("Disconnected");
					System.out.println("Day: " + i + "- MQTT request ID:" + j + "--> mesage published");
					validMQTTrequestsCount++;
					totalValid++;
				} catch (final MqttException me) {
					totalFail++;
					failedMQTTrequestsCount++;
					System.out.println("reason " + me.getReasonCode());
					System.out.println("msg " + me.getMessage());
					System.out.println("loc " + me.getLocalizedMessage());
					System.out.println("cause " + me.getCause());
					System.out.println("excep " + me);
					// me.printStackTrace();

					// if server is unavailable. We know so by using the responsecode.
					if (me.getReasonCode() == 32103) {

						System.out.println("Day: " + i + "- MQTT request ID:" + j
								+ "--> Error \n preparing incident to be submited to blockchain");

						// assuming the blockchain transaction fails
						String TransactionStatus = "fail";
						// trying submitting until otherwise (until transaction succeeds)
						while (TransactionStatus.equals("fail")) {
							System.out.println("Trying to submit an incident report to the blockchain\n");
							// submit incident to blockchain
							TransactionStatus = bridge.reportIncident(QoSmetric.getQoS(), failedMQTTrequestsCount,
									validMQTTrequestsCount);
							// Resting the count of valid MQTT requests
						}
						// once reporting succeeds, reset all current counters of valid of fail mqtt
						// requests
						validMQTTrequestsCount = 0;
						failedMQTTrequestsCount = 0;

					}
				}

			}

		}

		// End of the service period. Now submiting transaction to invoke the compliance
		// assessment by the smart contract
		System.out.println(
				"End of the service period. Now submiting transaction to invoke the compliance assessment by the smart contract");
		bridge.assessCompliance(reportID, QoSmetric.getQoS(), failedMQTTrequestsCount, validMQTTrequestsCount);
		System.out.println("checking whether the smart contract performs as it should");
		System.out.println("Total of Fail MQTT requests is ->>  " + totalFail);
		System.out.println("Total of valid MQTT requests is ->>  " + totalValid);
		System.out.println("ErrorRate should be: -->  " + (totalFail / totalValid) * 100);

	}

}
