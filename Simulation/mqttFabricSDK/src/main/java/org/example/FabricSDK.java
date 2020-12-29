package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;

public final class FabricSDK {
    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    // this where we grasp the QoS name.
    private Config QoSmetric;
    private Path walletDirectory;
    private Wallet wallet;
    private Path networkConfigFile;
    private Gateway.Builder builder;

    public FabricSDK() throws IOException {
        QoSmetric = new Config();

        // Load an existing wallet holding identities used to access the network.
        walletDirectory = Paths.get("Org1");
        wallet = Wallet.createFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        networkConfigFile = Paths.get("1 Org Local Fabric - Org1_connection.json");

        // Configure the gateway connection used to access the network.
        builder = Gateway.createBuilder().identity(wallet, "org1Admin").networkConfig(networkConfigFile)
                .discovery(true);

    }

    String createQoSreport(String QoS) throws IOException {

        // Create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("QoSenforce");
            // create a new monitoring report about a QoS metric.
            byte[] CreateCloudProviderRecord = contract.submitTransaction("createMonitoringReport", QoS, "0", "0");
            System.out.println(new String(CreateCloudProviderRecord, StandardCharsets.UTF_8));

        } catch (ContractException | TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ("");
    }


   /**
    * Report incidents to the smart contract
    * @param QoS the name of the QoS metric
    * @param failedMQTTrequestsCount count of Failed MQTT requests
    * @param validMQTTrequestsCount count of Valid MQTT requests
    * @return TransactionStatus : result of the transaction
    * @throws IOException
    */
    String reportIncident(String QoS,int failedMQTTrequestsCount, int validMQTTrequestsCount) throws IOException {

        String TransactionStatus = "Success";
        // Create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("QoSenforce");

            // a monitoring agents submits incident to the smart contract about a cloud
            // provider
            byte[] ReportViolationResult = contract.submitTransaction("reportViolation", QoS, Integer.toString(failedMQTTrequestsCount), Integer.toString(validMQTTrequestsCount));
            System.out.println(new String(ReportViolationResult, StandardCharsets.UTF_8));

        } catch (ContractException | TimeoutException e) {
            TransactionStatus = "fail";
            e.printStackTrace();
        } catch (InterruptedException e) {
            TransactionStatus = "fail";
            e.printStackTrace();
        }

        return TransactionStatus;
    }



    /**
     * Assess Compliance of Service Provider
     * @param ReportID Report ID 
     * @param QoS the name of the QoS metric
     * @param failedMQTTrequestsCount count of Failed MQTT requests
     * @param validMQTTrequestsCount count of Valid MQTT requests
     * @return
     * @throws IOException
     */

    String assessCompliance(String ReportID, String QoS,int failedMQTTrequestsCount, int validMQTTrequestsCount) throws IOException {

        // Create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("QoSenforce");

            // a monitoring agents submits incident to the smart contract about a cloud
            // provider
            byte[] ReportViolationResult = contract.submitTransaction("assessCompliance", ReportID,  QoS, Integer.toString(failedMQTTrequestsCount), Integer.toString(validMQTTrequestsCount));
            System.out.println(new String(ReportViolationResult, StandardCharsets.UTF_8));

        } catch (ContractException | TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ("");
    }


}