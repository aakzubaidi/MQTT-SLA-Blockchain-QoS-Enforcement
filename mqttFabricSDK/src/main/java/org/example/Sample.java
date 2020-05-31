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
import org.hyperledger.fabric.sdk.identity.Identity;

public final class Sample {
    static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}
    public static void main(String[] args) throws IOException {

        // Load an existing wallet holding identities used to access the network.
        Path walletDirectory = Paths.get("Org1");
        Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("1 Org Local Fabric - Org1_connection.json");

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "org1Admin")
                .networkConfig(networkConfigFile).discovery(true);

        // Create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("Ali");

            byte[] CreateCloudProviderRecord = contract.submitTransaction("createMonitoringReport", "cp1", "0", "0");
            System.out.println(new String(CreateCloudProviderRecord, StandardCharsets.UTF_8));
            // a monitoring agents submits incident to the smart contract about a cloud provider
            //byte[] ReportViolationResult = contract.submitTransaction("reportViolation", "cp1", "3", "1000");
            //System.out.println(new String(ReportViolationResult, StandardCharsets.UTF_8));

        } catch (ContractException | TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}