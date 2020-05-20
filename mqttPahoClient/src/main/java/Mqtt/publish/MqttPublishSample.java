/**
 * 
 */
package Mqtt.publish;

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String topic        = "2nd APR";
		String content      = "Message from MqttPublishSample";
		int qos             = 2;
		String broker       = "tcp://localhost:1883";
		String clientId     = "Client: ";
		MemoryPersistence persistence = new MemoryPersistence();


		for (int i = 1; i <= 2000; i++){
			try {
				MqttClient sampleClient = new MqttClient(broker, clientId+i, persistence);
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(true);
				connOpts.setConnectionTimeout(2);;
				System.out.println("Connecting to broker: "+broker);
				sampleClient.connect(connOpts);
				System.out.println("Connected");
				System.out.println("Publishing message: "+content);
				MqttMessage message = new MqttMessage(content.getBytes());
				message.setQos(qos);

				//for (int j = 1; j <= 400; j++) {

					content = "Iteration: "+ i;
					sampleClient.publish(topic, message);
					System.out.println("Message published: "+(i));

				//}

				//sampleClient.disconnect();
				//System.out.println("Disconnected");
				//System.exit(0);
			} catch(MqttException me) {
				System.out.println("reason "+me.getReasonCode());
				System.out.println("msg "+me.getMessage());
				System.out.println("loc "+me.getLocalizedMessage());
				System.out.println("cause "+me.getCause());
				System.out.println("excep "+me);
				me.printStackTrace();
				 //System.exit(0); 
			}

		}

	}

}
