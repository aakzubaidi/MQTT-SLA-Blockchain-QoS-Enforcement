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

public final class Sample {
    public static void main(String[] args) throws IOException, InterruptedException {


        Wallet fabricWallet;
        Gateway.Builder builder;
        Network network;
        Contract contract;
        String homedir = System.getProperty("user.home");
        Path walletPath = Paths.get(homedir, ".fabric-vscode", "environments", "1 Org Local Fabric", "wallets", "Org1");
        Path connectionProfilePath = Paths.get(homedir, ".fabric-vscode", "environments", "1 Org Local Fabric", "gateways", "Org1", "Org1.json");
        String identityName = "admin";
        
        fabricWallet = Wallet.createFileSystemWallet(walletPath);
        builder = Gateway.createBuilder();
        builder.identity(fabricWallet, identityName).networkConfig(connectionProfilePath).discovery(true);
    
        // Create a gateway connection
        
        try (Gateway gateway = builder.connect()) {

            
            // Obtain a smart contract deployed on the network.
             network = gateway.getNetwork("mychannel");
             contract = network.getContract("QoSenforce", "QoSContract");
            // Submit transactions that creates a record state for the performance of a cloud provider
            //byte[] CreateCloudProviderRecord = contract.submitTransaction("createMonitoringReport", "CP1", "0", "0");
            //System.out.println(new String(CreateCloudProviderRecord, StandardCharsets.UTF_8));
            // a monitoring agents submits incident to the smart contract about a cloud provider
            byte[] ReportViolationResult = contract.submitTransaction("reportViolation", "cp1", "3", "1000");
            System.out.println(new String(ReportViolationResult, StandardCharsets.UTF_8));

            // Evaluate transactions that query state from the ledger.
            //byte[] queryAllCarsResult = contract.evaluateTransaction("queryAllCars");
            //System.out.println(new String(queryAllCarsResult, StandardCharsets.UTF_8));

        } catch (ContractException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}